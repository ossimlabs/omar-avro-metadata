properties([
    parameters ([
        booleanParam(name: 'CLEAN_WORKSPACE', defaultValue: true, description: 'Clean the workspace at the end of the run'),
        string(name: 'DOCKER_REGISTRY_DOWNLOAD_URL', defaultValue: 'nexus-docker-private-group.ossim.io', description: 'Repository of docker images')
    ]),
    pipelineTriggers([
            [$class: "GitHubPushTrigger"]
    ]),
    [$class: 'GithubProjectProperty', displayName: '', projectUrlStr: 'https://github.com/ossimlabs/omar-wmts'],
    buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '3', daysToKeepStr: '', numToKeepStr: '20')),
    disableConcurrentBuilds()
])
podTemplate(
  containers: [
    containerTemplate(
      name: 'docker',
      image: 'docker:19.03.11',
      ttyEnabled: true,
      command: 'cat',
      privileged: true
    ),
    containerTemplate(
      image: "${DOCKER_REGISTRY_DOWNLOAD_URL}/omar-builder:latest",
      name: 'builder',
      command: 'cat',
      ttyEnabled: true
    ),
      containerTemplate(
      image: "${DOCKER_REGISTRY_DOWNLOAD_URL}/kubectl-aws-helm:latest",
      name: 'kubectl-aws-helm',
      command: 'cat',
      ttyEnabled: true,
      alwaysPullImage: true
    ),
    containerTemplate(
      image: "${DOCKER_REGISTRY_DOWNLOAD_URL}/alpine/helm:3.2.3",
      name: 'helm',
      command: 'cat',
      ttyEnabled: true
    ),
  ],
  volumes: [
    hostPathVolume(
      hostPath: '/var/run/docker.sock',
      mountPath: '/var/run/docker.sock'
    ),
  ]
)
node('omar-build'){

    stage("Checkout branch")
    {
        scmVars = checkout(scm)
        
        GIT_BRANCH_NAME = scmVars.GIT_BRANCH
        BRANCH_NAME = """${sh(returnStdout: true, script: "echo ${GIT_BRANCH_NAME} | awk -F'/' '{print \$2}'").trim()}"""
        sh """
        touch buildVersion.txt
        grep buildVersion gradle.properties | cut -d "=" -f2 > "buildVersion.txt"
        """
        preVERSION = readFile "buildVersion.txt"
        VERSION = preVERSION.substring(0, preVERSION.indexOf('\n'))

        GIT_TAG_NAME = "omar-avro-metadata" + "-" + VERSION  //webhook stuff
        ARTIFACT_NAME = "ArtifactName"

        script {
          if (BRANCH_NAME != 'master') {
            buildName "${VERSION} - ${BRANCH_NAME}-SNAPSHOT"
          } else {
            buildName "${VERSION} - ${BRANCH_NAME}"
          }
        }
    }

    stage("Load Variables")
    {
        withCredentials([string(credentialsId: 'o2-artifact-project', variable: 'o2ArtifactProject')]) {
            step ([$class: "CopyArtifact",
                projectName: o2ArtifactProject,
                filter: "common-variables.groovy",
                flatten: true])
        }

        load "common-variables.groovy"
        
        switch (BRANCH_NAME) {
        case "master":
          TAG_NAME = VERSION
          break

        case "dev":
          TAG_NAME = "latest"
          break

        default:
          TAG_NAME = BRANCH_NAME
          break
        }

      //  DOCKER_IMAGE_PATH = "${DOCKER_REGISTRY_PRIVATE_UPLOAD_URL}/omar-avro-metadata"
    }

    stage ("Assemble") {
        sh """
        gradle assemble \
            -PossimMavenProxy=${MAVEN_DOWNLOAD_URL}
        """
        archiveArtifacts "build/libs/*.jar"
    }

    stage ("Publish Nexus")
    {
        withCredentials([[$class: 'UsernamePasswordMultiBinding',
                        credentialsId: 'nexusCredentials',
                        usernameVariable: 'MAVEN_REPO_USERNAME',
                        passwordVariable: 'MAVEN_REPO_PASSWORD']])
        {
            sh """
            gradle publish \
                -PossimMavenProxy=${MAVEN_DOWNLOAD_URL}
            """
        }
    }

    stage ("Publish Docker App")
    {
        withCredentials([[$class: 'UsernamePasswordMultiBinding',
                        credentialsId: 'dockerCredentials',
                        usernameVariable: 'DOCKER_REGISTRY_USERNAME',
                        passwordVariable: 'DOCKER_REGISTRY_PASSWORD']])
        {
            // Run all tasks on the app. This includes pushing to OpenShift and S3.
            sh """
            gradle pushDockerImage \
                -PossimMavenProxy=${MAVEN_DOWNLOAD_URL}
            """
        }
    }

    stage('New Deploy'){
        container('kubectl-aws-helm') {
            withAWS(
            credentials: 'Jenkins IAM User',
            region: 'us-east-1'){
                if (BRANCH_NAME == 'master'){
                    //insert future instructions here
                }
                else if (BRANCH_NAME == 'dev') {
                    sh "aws eks --region us-east-1 update-kubeconfig --name gsp-dev-v2 --alias dev"
                    sh "kubectl config set-context dev --namespace=omar-dev"
                    sh "kubectl rollout restart deployment/omar-avro-metadata"   
                }
                else {
                    sh "echo Not deploying ${BRANCH_NAME} branch"
                }
            }
        }
    }
  
    stage("Clean Workspace")
    {
        if ("${CLEAN_WORKSPACE}" == "true")
            step([$class: 'WsCleanup'])
    }
}

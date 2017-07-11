package io.ossim.omaravrometadata

interface AvroMetadataService
{
    AvroMetadata addAvroMetadata(AvroMetadata avroMetadata)

    AvroMetadata getAvroMetadata(String imageId)

    List<AvroMetadata> listAvroMetadata()
}
package io.ossim.omaravrometadata

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AvroMetadataServiceImpl implements AvroMetadataService
{
    @Autowired
    private AvroMetadataRepository avroMetadataRepository

    @Override
    AvroMetadata addAvroMetadata(AvroMetadata avroMetadata)
    {
        avroMetadataRepository.save(avroMetadata)
    }

    @Override
    AvroMetadata getAvroMetadata(String imageId)
    {
        avroMetadataRepository.findByImageId(imageId)
    }

    @Override
    boolean deleteAvroMetadata(String imageId)
    {
        AvroMetadata toDelete = avroMetadataRepository.findByImageId(imageId)
        if (toDelete == null) return false

        avroMetadataRepository.delete(toDelete)
        return true
    }

    @Override
    List<AvroMetadata> listAvroMetadata()
    {
        (List<AvroMetadata>) avroMetadataRepository.findAll()
    }
}
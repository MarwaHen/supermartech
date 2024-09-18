package com.supermatech.service.impl;

import com.supermatech.domain.ImagePro;
import com.supermatech.repository.ImageProRepository;
import com.supermatech.service.ImageProService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.supermatech.domain.ImagePro}.
 */
@Service
@Transactional
public class ImageProServiceImpl implements ImageProService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageProServiceImpl.class);

    private final ImageProRepository imageProRepository;

    public ImageProServiceImpl(ImageProRepository imageProRepository) {
        this.imageProRepository = imageProRepository;
    }

    @Override
    public ImagePro save(ImagePro imagePro) {
        LOG.debug("Request to save ImagePro : {}", imagePro);
        return imageProRepository.save(imagePro);
    }

    @Override
    public ImagePro update(ImagePro imagePro) {
        LOG.debug("Request to update ImagePro : {}", imagePro);
        return imageProRepository.save(imagePro);
    }

    @Override
    public Optional<ImagePro> partialUpdate(ImagePro imagePro) {
        LOG.debug("Request to partially update ImagePro : {}", imagePro);

        return imageProRepository
            .findById(imagePro.getId())
            .map(existingImagePro -> {
                if (imagePro.getImgP_Path() != null) {
                    existingImagePro.setImgP_Path(imagePro.getImgP_Path());
                }
                if (imagePro.getPro_id() != null) {
                    existingImagePro.setPro_id(imagePro.getPro_id());
                }

                return existingImagePro;
            })
            .map(imageProRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagePro> findAll() {
        LOG.debug("Request to get all ImagePros");
        return imageProRepository.findAll();
    }

    public Page<ImagePro> findAllWithEagerRelationships(Pageable pageable) {
        return imageProRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImagePro> findOne(Long id) {
        LOG.debug("Request to get ImagePro : {}", id);
        return imageProRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ImagePro : {}", id);
        imageProRepository.deleteById(id);
    }
}

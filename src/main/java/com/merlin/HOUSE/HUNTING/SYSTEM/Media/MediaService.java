package com.merlin.HOUSE.HUNTING.SYSTEM.Media;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.ApartmentUnit;
import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.ApartmentUnitRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.InvalidFileException;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;
    private final Cloudinary cloudinary ;
    private final ApartmentUnitRepository apartmentUnitRepository;

    public MediaResponseDto uploadMedia(MultipartFile file, Long unitId ) throws IOException {
        Media media = new Media();

        String contentType = file.getContentType();

        if (contentType == null) {
            throw new InvalidFileException("Invalid content type");
        }

        if(contentType.startsWith("image/")) {
           media.setMediaType(MediaType.IMAGE);

       }else if(contentType.startsWith("video/")) {
           media.setMediaType(MediaType.VIDEO);
       }
       else  {
           throw new InvalidFileException("Only Images and Videos are supported");
       }

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String url = uploadResult.get("secure_url").toString();
        String publicId = uploadResult.get("public_id").toString();

        media.setPublicId(publicId);
        media.setUrl(url);

        ApartmentUnit apartmentUnit = apartmentUnitRepository.findById(unitId)
                .orElseThrow(() -> new ResourceNotFound("apartment unit not found"));

        media.setApartmentUnit(apartmentUnit);
        media.setCreatedAt(LocalDateTime.now());

        var savedMedia = mediaRepository.save(media);

        return mediaMapper.toMediaResponseDto(savedMedia);

    }

    public void deleteMedia(Long mediaId) throws IOException {

        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFound("media not found"));

        cloudinary.uploader().destroy(media.getPublicId(), ObjectUtils.emptyMap());

        mediaRepository.delete(media);
    }

    public List<MediaResponseDto> getMediaByUnit(Long unitId) {
        return mediaRepository.findMediaByApartmentUnit(unitId)
                .stream()
                .map(mediaMapper ::toMediaResponseDto)
                .toList();
    }



}

package com.merlin.HOUSE.HUNTING.SYSTEM.Campus;

import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.BusinessRuleException;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.ResourceNotFound;
import com.merlin.HOUSE.HUNTING.SYSTEM.Location.Location;
import com.merlin.HOUSE.HUNTING.SYSTEM.Location.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampusService {

    private final CampusRepository campusRepository;
    private final CampusMapper campusMapper;
    private final LocationRepository locationRepository;


    public CampusResponseDto createCampus(CampusDto dto){
        Campus campus = campusMapper.toCampus(dto);

        Location campusLocation  = new Location(dto.latitude(), dto.longitude());
        var savedLocation = locationRepository.save(campusLocation);
        campus.setLocation(savedLocation);
        var savedCampus = campusRepository.save(campus);

        return campusMapper.toCampusResponseDto(savedCampus);
    }

    public CampusResponseDto updateCampus(CampusDto dto, Long campusId){
        Campus campus = campusRepository.findById(campusId)
                .orElseThrow(()-> new ResourceNotFound("Campus not found"));

        if(dto.campusName() != null){
            campus.setCampusName(dto.campusName());
        }
        if(dto.latitude() != null && dto.longitude() != null){
            Location campusLocation = new Location(dto.latitude(), dto.longitude());
            var savedLocation = locationRepository.save(campusLocation);
            campus.setLocation(savedLocation);
        }

        return campusMapper.toCampusResponseDto(campusRepository.save(campus));
    }

    public List<CampusResponseDto> getAllCampuses(){
        return campusRepository.findAll()
                .stream()
                .map(campusMapper :: toCampusResponseDto)
                .toList();
    }

    public CampusResponseDto getCampusById(Long campusId){
        Campus campus = campusRepository.findById(campusId)
                .orElseThrow(()-> new ResourceNotFound("Campus not found"));

        return campusMapper.toCampusResponseDto(campus);
    }

    public void activateCampus(Long campusId){
        Campus campus = campusRepository.findById(campusId)
                .orElseThrow(()-> new ResourceNotFound("Campus not found"));

        campus.setActive(true);
        campusRepository.save(campus);

    }

    public void deactivateCampus(Long campusId){
        Campus campus = campusRepository.findById(campusId)
                .orElseThrow(() -> new ResourceNotFound("Campus not found"));
        campus.setActive(false);
        campusRepository.save(campus);
    }

    public void deleteCampus(Long campusId){
        Campus campus = campusRepository.findById(campusId)
                .orElseThrow(()-> new ResourceNotFound("Campus not found"));

        if (campus.getApartments().isEmpty()){
            campusRepository.delete(campus);
        }else {
            throw new BusinessRuleException("Cant delete this campus because it has active apartments");
        }

    }
}

package ru.dragomirov.utils;

import org.modelmapper.ModelMapper;
import ru.dragomirov.dto.request.LocationRequestDTO;
import ru.dragomirov.dto.response.LocationResponseDTO;
import ru.dragomirov.entities.Location;

public class MappingUtil {
    private static final ModelMapper MODEL_MAPPER;

    static {
        MODEL_MAPPER = new ModelMapper();

        MODEL_MAPPER.typeMap(LocationRequestDTO.class, Location.class)
                .addMappings(mapper -> {
                    mapper.map(LocationRequestDTO::getLatitude, Location::setLatitude);
                    mapper.map(LocationRequestDTO::getLongitude, Location::setLongitude);
                });
    }

    public static Location locationToEntity(LocationRequestDTO dto) {
        return MODEL_MAPPER.map(dto, Location.class);
    }

    public static LocationResponseDTO locationToDTO(Location entity) {
        return MODEL_MAPPER.map(entity, LocationResponseDTO.class);
    }
}

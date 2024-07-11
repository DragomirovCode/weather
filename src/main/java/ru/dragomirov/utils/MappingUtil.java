package ru.dragomirov.utils;

import org.modelmapper.ModelMapper;
import ru.dragomirov.dto.request.WeatherByLocationRequestDTO;
import ru.dragomirov.dto.response.LocationResponseDTO;
import ru.dragomirov.entities.Location;

public class MappingUtil {
    private static final ModelMapper MODEL_MAPPER;

    static {
        MODEL_MAPPER = new ModelMapper();

        MODEL_MAPPER.typeMap(WeatherByLocationRequestDTO.class, Location.class)
                .addMappings(mapper -> {
                    mapper.map(WeatherByLocationRequestDTO::getLatitude, Location::setLatitude);
                    mapper.map(WeatherByLocationRequestDTO::getLongitude, Location::setLongitude);
                });
    }

    public static Location locationToEntity(WeatherByLocationRequestDTO dto) {
        return MODEL_MAPPER.map(dto, Location.class);
    }

    public static LocationResponseDTO locationToDTO(Location entity) {
        return MODEL_MAPPER.map(entity, LocationResponseDTO.class);
    }
}

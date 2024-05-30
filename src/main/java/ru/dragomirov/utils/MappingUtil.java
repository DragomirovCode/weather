package ru.dragomirov.utils;

import org.modelmapper.ModelMapper;
import ru.dragomirov.dto.request.LocationRequestDTO;
import ru.dragomirov.entities.Location;

public class MappingUtil {
    private static final ModelMapper MODEL_MAPPER;

    static {
        MODEL_MAPPER = new ModelMapper();
    }

    public static Location locationToEntity(LocationRequestDTO dto) {
        return MODEL_MAPPER.map(dto, Location.class);
    }
}

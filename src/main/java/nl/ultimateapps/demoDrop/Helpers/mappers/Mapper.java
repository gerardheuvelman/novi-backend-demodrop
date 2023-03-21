//package nl.ultimateapps.demoDrop.Helpers.mappers;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public abstract class Mapper<E, D> {
//
//    private Class<? extends E > myClassClass = E.getClass();
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    public static D mapToDto(E entity, Class<D> dtoClass) {
//        return modelMapper.map(entity, dtoClass);
//    }
//
//    public static E mapToModel(D dto, Class<E> entityClass) {
//        return modelMapper.map(dto, entityClass);
//    }
//}
//

package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters;
import java.util.List;

public interface ClassToDto<F,T> {
    T convert(F source);
    List<T> convert(List<F> source);
}

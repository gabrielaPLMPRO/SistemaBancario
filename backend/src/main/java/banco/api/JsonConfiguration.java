package banco.api;

import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.*;

@Configuration(proxyBeanMethods=false)
public class JsonConfiguration {
    @Bean Jackson2ObjectMapperBuilderCustomizer tiposEstritos() {
        return builder -> builder.postConfigurer(mapper -> {
            mapper.coercionConfigFor(LogicalType.Integer)
                .setCoercion(CoercionInputShape.Float,CoercionAction.Fail)
                .setCoercion(CoercionInputShape.String,CoercionAction.Fail);
            mapper.coercionConfigFor(LogicalType.Textual)
                .setCoercion(CoercionInputShape.Integer,CoercionAction.Fail)
                .setCoercion(CoercionInputShape.Float,CoercionAction.Fail)
                .setCoercion(CoercionInputShape.Boolean,CoercionAction.Fail);
        });
    }
}

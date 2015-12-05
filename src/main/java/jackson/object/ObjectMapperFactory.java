package jackson.object;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.BeanAsArraySerializer;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import com.fasterxml.jackson.databind.util.NameTransformer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Povka on 2015.12.04.
 */
public class ObjectMapperFactory {

    public static class CustomModifier extends BeanSerializerModifier{

        @Override
        public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
            if (serializer instanceof BeanSerializerBase) {
                return new MetaSerializer(
                        (BeanSerializerBase) serializer);
            }
            return serializer;
        }
    }

    public static class MetaSerializer extends BeanSerializerBase {
        protected MetaSerializer(JavaType type, BeanSerializerBuilder builder, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties) {
            super(type, builder, properties, filteredProperties);
        }

        public MetaSerializer(BeanSerializerBase src, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties) {
            super(src, properties, filteredProperties);
        }

        protected MetaSerializer(BeanSerializerBase src, ObjectIdWriter objectIdWriter) {
            super(src, objectIdWriter);
        }

        protected MetaSerializer(BeanSerializerBase src, ObjectIdWriter objectIdWriter, Object filterId) {
            super(src, objectIdWriter, filterId);
        }

        protected MetaSerializer(BeanSerializerBase src, String[] toIgnore) {
            super(src, toIgnore);
        }

        protected MetaSerializer(BeanSerializerBase src) {
            super(src);
        }

        protected MetaSerializer(BeanSerializerBase src, NameTransformer unwrapper) {
            super(src, unwrapper);
        }

        @Override
        public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter) {
            return null;
        }

        @Override
        protected BeanSerializerBase withIgnorals(String[] toIgnore) {
            return new MetaSerializer(this, toIgnore);
        }

        @Override
        protected BeanSerializerBase asArraySerializer() {
            if ((_objectIdWriter == null)
                    && (_anyGetterWriter == null)
                    && (_propertyFilterId == null)
                    ) {
                return new BeanAsArraySerializer(this);
            }
            // already is one, so:
            return this;
        }

        @Override
        protected BeanSerializerBase withFilterId(Object filterId) {
            return new MetaSerializer(this, _objectIdWriter, filterId);
        }

        @Override
        public void serialize(Object bean, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            if (_objectIdWriter != null) {
                _serializeWithObjectId(bean, jgen, provider, true);
                return;
            }
            jgen.writeStartObject();
            appendMeta(bean, jgen);
            if (_propertyFilterId != null) {
                serializeFieldsFiltered(bean, jgen, provider);
            } else {
                serializeFields(bean, jgen, provider);
            }
            jgen.writeEndObject();

        }

        private void appendMeta(Object bean, JsonGenerator jgen) throws IOException {
            Map<String, Map<String, String>> meta = new LinkedHashMap<String, Map<String, String>>();
            for(Field field : bean.getClass().getDeclaredFields()) {
                if(field.getDeclaredAnnotation(MetaTag.class) != null) {
                    Map<String, String> fieldInfo = new LinkedHashMap<String, String>();
                    fieldInfo.put("type", field.getType().getSimpleName());
                    meta.put(field.getName(), fieldInfo);
                }
            }
            if(!meta.isEmpty()) {
                jgen.writeObjectField("_meta_", meta);
            }
        }
    }


    public static class CustomModule extends SimpleModule {

        @Override
        public void setupModule(SetupContext context) {
            context.addBeanSerializerModifier(new CustomModifier());
        }
    }

    public ObjectMapper createMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new CustomModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }
}

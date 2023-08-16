package mgm.utilities;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.Map;

@Component
public class CustomModel implements Model {
    @Override public Model addAttribute(String attributeName, Object attributeValue) {
        return null;
    }

    @Override public Model addAttribute(Object attributeValue) {
        return null;
    }

    @Override public Model addAllAttributes(Collection<?> attributeValues) {
        return null;
    }

    @Override public Model addAllAttributes(Map<String, ?> attributes) {
        return null;
    }

    @Override public Model mergeAttributes(Map<String, ?> attributes) {
        return null;
    }

    @Override public boolean containsAttribute(String attributeName) {
        return false;
    }

    @Override public Object getAttribute(String attributeName) {
        return null;
    }

    @Override public Map<String, Object> asMap() {
        return null;
    }
}

package ghostkitchen.core;

import java.util.HashMap;
import java.util.Map;

import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;

public interface ImageCache {
    Map<String, Image> cache = new HashMap<>();

    default Image getImage(String relativePath) {
        cache.computeIfAbsent(relativePath, k -> ImageUtils.loadImage("resources/images/" + relativePath));
        return cache.get(relativePath);
    }
}

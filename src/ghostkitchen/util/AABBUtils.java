package ghostkitchen.util;

import br.com.davidbuzatto.jsge.collision.aabb.AABB;

public class AABBUtils {
    private AABBUtils() {}

    public static AABB copy(AABB aabb) {
        AABB copy = new AABB(aabb.x1, aabb.y1, aabb.x2, aabb.y2, aabb.referencedObject);
        copy.type = aabb.type;
        copy.active = aabb.active;
        copy.nearby = aabb.nearby;
        return copy;
    }
}

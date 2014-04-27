package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * Slow up and down bobbing.
 *
 * @todo create some sort of transformation stack so we can combine leading and secondary transformations correctly.
 * @author Daan van Yperen
 */
public class Bobbing extends Component {

    public float originX;
    public float originY;
    public float speed;
    public float maxDistanceY;
    public float maxDistanceX;
    public float age;

    public Bobbing(float maxDistanceX, float maxDistanceY, float originX, float originY, float speed) {
        this.maxDistanceX = maxDistanceX;
        this.maxDistanceY = maxDistanceY;
        this.originX = originX;
        this.originY = originY;
        this.speed = speed;
    }
}

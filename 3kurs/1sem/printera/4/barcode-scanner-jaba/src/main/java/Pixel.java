import java.util.*;

public class Pixel implements Comparable<Pixel> {

    public Integer pos;
    public Integer color;

    public Pixel(Integer pos, Integer color) {
        this.pos = pos;
        this.color = color;
    }

    @Override
    public int compareTo(Pixel o) {
        return pos.compareTo(o.pos);
    }

    @Override
    public String toString() {
        return "java.Pixel{" +
                "pos=" + pos +
                ", color=" + color +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pixel pixel = (Pixel) o;
        return Objects.equals(pos, pixel.pos) &&
                Objects.equals(color, pixel.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, color);
    }
}

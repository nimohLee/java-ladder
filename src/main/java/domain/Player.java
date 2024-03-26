package domain;

import java.util.Objects;

public class Player {
    public static final String NAME_LENGTH_CANT_OVER_FIVE = "이름은 5자를 초과할 수 없습니다.";
    private final String name;
    public static Player from(String name) {
        return new Player(name);
    }

    private Player(String name) {
        if (name.length() > 5) {
            throw new IllegalArgumentException(NAME_LENGTH_CANT_OVER_FIVE);
        }
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}

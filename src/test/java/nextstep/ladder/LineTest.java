package nextstep.ladder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LineTest {
    @Test
    void create() {
        assertThat(new Line(Spork.of(true, false, true))).isNotNull();
    }

    @DisplayName("오른쪽에 발판이 존재하는지 알 수 있다")
    @Test
    void hasSporkRightSide() {
        Line line = new Line(Spork.of(true, false, true));
        assertAll(
                () -> assertThat(line.hasSporkRightSide(Position.of(0))).isTrue(),
                () -> assertThat(line.hasSporkRightSide(Position.of(1))).isFalse(),
                () -> assertThat(line.hasSporkRightSide(Position.of(2))).isTrue()
        );
    }

    @DisplayName("발판은 갯수로 생성할 수 있다.")
    @Test
    void createSpokeFromCount() {
        assertThat(Spork.fromCount(3, createTestingObject(true, false, true))).isNotNull();
    }

    @DisplayName("제네레이터을 통해 생성한 값을 테스트한다")
    @Test
    void sporkCreateFromGenerator() {
        assertThat(Spork.fromCount(3, createTestingObject(true, false, true)))
                .isEqualTo(Spork.of(true, false, true));
    }

    private TestingBooleanGenerator createTestingObject(Boolean... booleans) {
        return new TestingBooleanGenerator(booleans);
    }

    /**
     * |-----|     |-----| 의 경우 첫번째는 1~2 구간에는 발판이 있고 2~3구간에는 발판이 없고 3~4 구간에는 발판이 있다.
     * 이를 Spork.of(true, false, true) 로 표현한다
     */
    private static class Spork {
        private final List<Boolean> list = new ArrayList<>();
        public static Spork of(Boolean... existsSpoke) {
            return Stream.of(existsSpoke)
                    .collect(collect());
        }

        public static Spork fromCount(int count, BooleanGenerator booleanGenerator) {
            return IntStream.range(0, count)
                    .mapToObj(number -> booleanGenerator.nextBoolean())
                    .collect(collect());
        }

        private static Collector<Boolean, Spork, Spork> collect() {
            return Collector.of(Spork::new, Spork::add, (spork, spork2) -> {
                throw new UnsupportedOperationException("병렬처리는 지원하지 않습니다.");
            });
        }

        private void add(Boolean next) {
            if (list.isEmpty()) {
                list.add(next);
                return;
            }

            Boolean previousBoolean = last();
            if (previousBoolean) {
                list.add(Boolean.FALSE);
                return;
            }

            list.add(next);
        }

        private Boolean last() {
            return list.get(list.size() - 1);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Spork spork = (Spork) o;
            return list.equals(spork.list);
        }

        @Override
        public int hashCode() {
            return Objects.hash(list);
        }

        @Override
        public String toString() {
            return "Spork{" +
                    "list=" + list +
                    '}';
        }

        public Stream<Boolean> stream() {
            return list.stream();
        }
    }

    private static class Line {

        private final List<Boolean> cross;

        public Line(Spork spork) {
            cross = spork.stream().collect(toList());
        }

        public boolean hasSporkRightSide(Position position) {
            return cross.get(position.crossPosition);
        }
    }

    private static class Position {
        public int crossPosition;

        public Position(int crossPosition) {
            this.crossPosition = crossPosition;
        }

        public static Position of(int position) {
            return new Position(position);
        }
    }
}

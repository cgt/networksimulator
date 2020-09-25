package name.cgt.networksimulator;

import java.util.Arrays;
import java.util.Objects;

class Frame {
    private final NetworkAdapterAddress source;
    private final NetworkAdapterAddress destination;
    private final byte[] data;

    public Frame(NetworkAdapterAddress source, NetworkAdapterAddress destination, byte[] data) {
        this.source = source;
        this.destination = destination;
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Frame frame = (Frame) o;
        return Objects.equals(source, frame.source) &&
          Objects.equals(destination, frame.destination) &&
          Arrays.equals(data, frame.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(source, destination);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "Frame{" +
          "source=" + source +
          ", destination=" + destination +
          ", data=" + Arrays.toString(data) +
          '}';
    }

    public boolean isFor(NetworkAdapterAddress address) {
        return address.equals(destination);
    }
}

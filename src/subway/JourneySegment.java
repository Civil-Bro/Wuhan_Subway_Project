package subway;

class JourneySegment {
    private String line;
    private Station from;
    private Station to;

    public JourneySegment(String line, Station from, Station to) {
        this.line = line;
        this.from = from;
        this.to = to;
    }

    public String getLine() {
        return line;
    }

    public Station getFrom() {
        return from;
    }

    public Station getTo() {
        return to;
    }

    @Override
    public String toString() {
        return String.format("乘坐%s 从 %s 到 %s", line, from.getName(), to.getName());
    }
}

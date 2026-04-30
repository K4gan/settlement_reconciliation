package dev.portfolio.settlementreconciliation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

record Signal(String name, BigDecimal weight, int score) {}
record Explanation(String decision, List<String> reasons, BigDecimal total) {}

final class Engine {
    Explanation evaluate(List<Signal> signals) {
        var sorted = new ArrayList<>(signals);
        sorted.sort(Comparator.comparing(Signal::score).reversed());
        var reasons = new ArrayList<String>();
        var total = BigDecimal.ZERO;
        for (var signal : sorted) {
            var contribution = signal.weight().multiply(BigDecimal.valueOf(signal.score()));
            total = total.add(contribution);
            reasons.add(signal.name() + "=" + contribution);
        }
        var decision = total.compareTo(BigDecimal.valueOf(120)) >= 0 ? "review" : "approve";
        return new Explanation(decision, reasons, total);
    }
}

public class App {
    public static void main(String[] args) {
        var engine = new Engine();
        var explanation = engine.evaluate(List.of(
            new Signal("velocity", BigDecimal.valueOf(1.4), 64),
            new Signal("exposure", BigDecimal.valueOf(0.9), 38)
        ));
        System.out.println(explanation.decision() + " " + explanation.total());
    }
}

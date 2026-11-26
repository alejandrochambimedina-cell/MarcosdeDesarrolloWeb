import pe.edu.techlab.domain.CourseRules;
import java.math.BigDecimal;
public class RulesCheck {
    static void equal(Object a, Object b) { if(!a.equals(b)) throw new AssertionError(a+" != "+b); }
    static void rejects(Runnable run) { try { run.run(); } catch(IllegalArgumentException e) { return; }
        throw new AssertionError("Se esperaba rechazo"); }
    public static void main(String[] args) {
        equal(CourseRules.normalizeName("  Java  "),"Java");
        equal(CourseRules.validatePrice(new BigDecimal("0")),new BigDecimal("0.00"));
        equal(CourseRules.validatePrice(new BigDecimal("999999.99")),new BigDecimal("999999.99"));
        rejects(() -> CourseRules.normalizeName(" "));
        rejects(() -> CourseRules.normalizeName("x".repeat(121)));
        rejects(() -> CourseRules.validatePrice(new BigDecimal("-0.01")));
        rejects(() -> CourseRules.validatePrice(new BigDecimal("1000000")));
        rejects(() -> CourseRules.validatePrice(new BigDecimal("1.234")));
        System.out.println("PASS: 8 comprobaciones de reglas de dominio");
    }
}

package pt.ulisboa.tecnico.cmov.airdesk.exceptions;

public class QuotaExceededException extends Exception {
    private int excess;

    public QuotaExceededException(int excess) {
        this.excess = excess;
    }

    public int getExcess() {
        return excess;
    }
}

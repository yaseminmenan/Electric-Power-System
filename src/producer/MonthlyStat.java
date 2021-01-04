package producer;

import java.util.List;

public class MonthlyStat {
    private int month;
    private List<Long> distributorsIds;

    public MonthlyStat(int month, List<Long> distributorsIds) {
        this.month = month;
        this.distributorsIds = distributorsIds;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public List<Long> getDistributorsIds() {
        return distributorsIds;
    }

    public void setDistributorsIds(List<Long> distributorsIDs) {
        this.distributorsIds = distributorsIds;
    }

    @Override
    public String toString() {
        return "MonthlyStat{" +
                "month=" + month +
                ", distributorsIDs=" + distributorsIds +
                '}'+"\n";
    }
}

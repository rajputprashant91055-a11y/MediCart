package ui;

import service.PharmacyMatcherService;
import java.util.Map;

public class SmartPharmacyMatcher {

    private static final PharmacyMatcherService matcherService = new PharmacyMatcherService();

    public static class MatchResult {
        private final boolean fullMatch;
        private final String pharmacyName;
        private final String message;

        public MatchResult(boolean fullMatch, String pharmacyName, String message) {
            this.fullMatch = fullMatch;
            this.pharmacyName = pharmacyName;
            this.message = message;
        }

        public boolean isFullMatch() {
            return fullMatch;
        }

        public String getPharmacyName() {
            return pharmacyName;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * Database-backed 100% stock pharmacy matching.
     * Searches all registered pharmacies in MySQL; requires 100% fulfillment of all items.
     * Prevents partial orders and automatic substitutions.
     */
    public static MatchResult findFullMatch(Map<String, Integer> requiredMedicines) {
        if (requiredMedicines == null || requiredMedicines.isEmpty()) {
            return new MatchResult(false, null, "No medicines were provided for matching.");
        }
        try {
            PharmacyMatcherService.MatchResult result = matcherService.findFullMatch(requiredMedicines);
            return new MatchResult(
                    result.isFullMatch(),
                    result.getPharmacyName(),
                    result.getMessage()
            );
        } catch (Exception e) {
            return new MatchResult(
                    false,
                    null,
                    "Database matching error: " + e.getMessage()
            );
        }
    }
}

package gr.aueb.cf.recipeapp.util;

import java.util.*;

public class IngredientFuzzyMatcher {

    public record MatchResult(List<String> usedIngredients, Map<String, String> corrections, List<String> unknown) {}

    public static MatchResult mapToKnown(List<String> rawTokens, Collection<String> knownNames) {

        Set<String> known = new HashSet<>();
        for (String k : knownNames) {
            if (k != null && !k.isBlank()) known.add(k.trim().toLowerCase());
        }

        List<String> used = new ArrayList<>();
        Map<String, String> corrections = new LinkedHashMap<>();
        List<String> unknown = new ArrayList<>();

        for (String raw : rawTokens) {
            if (raw == null) continue;
            String token = raw.trim().toLowerCase();
            if (token.isBlank()) continue;

            String best = bestMatch(token, known);

            if (best == null) {
                unknown.add(token);
                continue;
            }

            used.add(best);
            if (!best.equals(token)) corrections.put(token, best);
        }

        used = used.stream().distinct().toList();
        unknown = unknown.stream().distinct().toList();

        return new MatchResult(used, corrections, unknown);
    }

    private static String bestMatch(String token, Set<String> known) {

        if (known.contains(token)) return token;

        String singular = singularize(token);
        if (known.contains(singular)) return singular;

        String best = null;
        double bestScore = 0.0;

        for (String k : known) {
            double score = score(token, k);
            if (score > bestScore) {
                bestScore = score;
                best = k;
            }
        }

        return bestScore >= 1.0 ? best : null;
    }

    private static String singularize(String w) {
        if (w.endsWith("s") && w.length() > 3) return w.substring(0, w.length() - 1);
        return w;
    }

    private static double score(String a, String b) {

        if (a.equals(b)) return 2.0;

        if (a.equals(singularize(b)) || b.equals(singularize(a))) return 1.4;

        if (prefixRatio(a, b) >= 0.9) return 1.2;

        if (a.length() >= 3 && b.length() >= 3 && levenshtein(a, b) <= 1) return 1.1;

        return 0.0;
    }

    private static double prefixRatio(String a, String b) {

        String shortS = a.length() <= b.length() ? a : b;
        String longS  = a.length() <= b.length() ? b : a;

        if (shortS.length() < 3) return 0.0;

        if (longS.startsWith(shortS)) {
            return (double) shortS.length() / (double) longS.length();
        }
        return 0.0;
    }

    private static int levenshtein(String a, String b) {

        int n = a.length();
        int m = b.length();

        int[] prev = new int[m + 1];
        int[] cur  = new int[m + 1];

        for (int j = 0; j <= m; j++) prev[j] = j;

        for (int i = 1; i <= n; i++) {
            cur[0] = i;
            char ca = a.charAt(i - 1);

            for (int j = 1; j <= m; j++) {
                char cb = b.charAt(j - 1);
                int cost = (ca == cb) ? 0 : 1;

                cur[j] = Math.min(
                        Math.min(cur[j - 1] + 1, prev[j] + 1),
                        prev[j - 1] + cost
                );
            }

            int[] tmp = prev;
            prev = cur;
            cur = tmp;
        }

        return prev[m];
    }
}

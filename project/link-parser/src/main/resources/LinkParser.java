public sealed interface LinkHandler permits GitHubLinkHandler, StackOverflowLinkHandler {
    String handleLink(String url);
}

public record GitHubLinkHandler() implements LinkHandler {
    private static final Pattern GITHUB_PATTERN = Pattern.compile("^https?://github.com/(\\w+)/([\\w-]+)$");

    @Override
    public String handleLink(String url) {
        Matcher matcher = GITHUB_PATTERN.matcher(url);
        if (matcher.matches()) {
            String username = matcher.group(1);
            String repository = matcher.group(2);
            return username + "/" + repository;
        } else {
            return null;
        }
    }
}

public record StackOverflowLinkHandler() implements LinkHandler {
    private static final Pattern STACKOVERFLOW_PATTERN = Pattern.compile("^https?://stackoverflow.com/questions/(\\d+)/.*$");

    @Override
    public String handleLink(String url) {
        Matcher matcher = STACKOVERFLOW_PATTERN.matcher(url);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}

public class LinkParser {
    private final List<LinkHandler> handlers;

    public LinkParser() {
        handlers = List.of(new GitHubLinkHandler(), new StackOverflowLinkHandler());
    }

    public String parseLink(String url) {
        for (LinkHandler handler : handlers) {
            String result = handler.handleLink(url);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}

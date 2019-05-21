import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.Serializable;

public class Stream implements LifecycleManager, Serializable {
    private static String _consumerKey = System.getenv().get("CONSUMER_KEY");
    private static String _consumerSecret = System.getenv().get("CONSUMER_SECRET");
    private static String _accessToken = System.getenv().get("TWITTER_ACCESS_TOKEN");
    private static String _accessTokenSecret = System.getenv().get("ACCESS_TOKEN_SECRET");

    private TwitterStream Ts;
    private StatusListener Listener;

    public static TwitterStream getTwitterStreamInstance() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(_consumerKey)
                .setOAuthConsumerSecret(_consumerSecret)
                .setOAuthAccessToken(_accessToken)
                .setOAuthAccessTokenSecret(_accessTokenSecret);
        TwitterStreamFactory tf = new TwitterStreamFactory(cb.build());
        return tf.getInstance();
    }

    public void start() {
        Ts = getTwitterStreamInstance();
        Listener = new StatusListener() {
            public void onStatus(Status status) {
                System.out.println("help");
                Tweet tt = new Tweet(status.getUser().getName(), status.getText(), status.getCreatedAt().toString());
                System.out.println(tt.getUsername() + ":" + " " + tt.getTweetText());
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
            public void onTrackLimitationNotice(int i) {}
            public void onScrubGeo(long l, long l1) {}
            public void onStallWarning(StallWarning stallWarning) {}
            public void onException(Exception e) {}
        };
        Ts.addListener(Listener);

        String terms = "sergio";
        FilterQuery query = new FilterQuery();
        query.track(terms.split(","));
        Ts.filter(query);

    }

    public void stop() {
        Ts.shutdown();
    }
}

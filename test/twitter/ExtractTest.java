/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    public void testGetTimespanSingleTweet() {
        Instant timestamp = Instant.parse("2024-09-29T10:15:30Z");
        Tweet tweet = new Tweet(1, "user1", "Hello world", timestamp);
        List<Tweet> tweets = Arrays.asList(tweet);

        Timespan timespan = Extract.getTimespan(tweets);
        assertEquals(timestamp, timespan.getStart());
        assertEquals(timestamp, timespan.getEnd());
    }

    @Test
    public void testGetTimespanMultipleTweets() {
        Instant timestamp1 = Instant.parse("2024-09-29T10:15:30Z");
        Instant timestamp2 = Instant.parse("2024-09-29T12:15:30Z");
        Instant timestamp3 = Instant.parse("2024-09-29T11:15:30Z");

        Tweet tweet1 = new Tweet(1, "user1", "First tweet", timestamp1);
        Tweet tweet2 = new Tweet(2, "user2", "Second tweet", timestamp2);
        Tweet tweet3 = new Tweet(3, "user3", "Third tweet", timestamp3);

        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);
        Timespan timespan = Extract.getTimespan(tweets);

        assertEquals(timestamp1, timespan.getStart());
        assertEquals(timestamp2, timespan.getEnd());
    }

    @Test
    public void testGetTimespanOutOfOrderTweets() {
        Instant timestamp1 = Instant.parse("2024-09-29T10:15:30Z");
        Instant timestamp2 = Instant.parse("2024-09-29T12:15:30Z");
        Instant timestamp3 = Instant.parse("2024-09-29T11:15:30Z");

        Tweet tweet1 = new Tweet(1, "user1", "First tweet", timestamp2);
        Tweet tweet2 = new Tweet(2, "user2", "Second tweet", timestamp3);
        Tweet tweet3 = new Tweet(3, "user3", "Third tweet", timestamp1);

        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);
        Timespan timespan = Extract.getTimespan(tweets);

        assertEquals(timestamp1, timespan.getStart());
        assertEquals(timestamp2, timespan.getEnd());
    }
    
 // Test case for a single mention in a single tweet
    @Test
    public void testGetMentionedUsersSingleMention() {
        Tweet tweet1 = new Tweet(1, "user1", "@Alice is great!", Instant.now());
        List<Tweet> tweets = Arrays.asList(tweet1);

        Set<String> expectedMentions = new HashSet<>(Arrays.asList("alice"));
        assertEquals(expectedMentions, Extract.getMentionedUsers(tweets));
    }

    // Test case for multiple mentions in a single tweet
    @Test
    public void testGetMentionedUsersMultipleMentions() {
        Tweet tweet1 = new Tweet(1, "user1", "@Bob @Alice and @Charlie are great!", Instant.now());
        List<Tweet> tweets = Arrays.asList(tweet1);

        Set<String> expectedMentions = new HashSet<>(Arrays.asList("bob", "alice", "charlie"));
        assertEquals(expectedMentions, Extract.getMentionedUsers(tweets));
    }

    // Test case for multiple tweets with mentions
    @Test
    public void testGetMentionedUsersMultipleTweets() {
        Tweet tweet1 = new Tweet(1, "user1", "@Bob is great!", Instant.now());
        Tweet tweet2 = new Tweet(2, "user2", "@Alice is amazing!", Instant.now());
        Tweet tweet3 = new Tweet(3, "user3", "@Charlie is cool!", Instant.now());
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);

        Set<String> expectedMentions = new HashSet<>(Arrays.asList("bob", "alice", "charlie"));
        assertEquals(expectedMentions, Extract.getMentionedUsers(tweets));
    }

    // Test case to check that email addresses don't count as mentions
    @Test
    public void testGetMentionedUsersNoEmailAsMention() {
        Tweet tweet1 = new Tweet(1, "user1", "Contact me at alice@example.com", Instant.now());
        List<Tweet> tweets = Arrays.asList(tweet1);

        Set<String> expectedMentions = new HashSet<>();
        assertEquals(expectedMentions, Extract.getMentionedUsers(tweets));
    }

    // Test case for no mentions
    @Test
    public void testGetMentionedUsersNoMentions() {
        Tweet tweet1 = new Tweet(1, "user1", "No mentions here!", Instant.now());
        List<Tweet> tweets = Arrays.asList(tweet1);

        Set<String> expectedMentions = new HashSet<>();
        assertEquals(expectedMentions, Extract.getMentionedUsers(tweets));
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}

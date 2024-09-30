/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

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
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }
    
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }
    
    @Test
    public void testContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }

    @Test
    public void testWrittenBy() {
      
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet(1, "user1", "Hello World", Instant.parse("2024-09-30T10:00:00Z")));
        tweets.add(new Tweet(2, "user2", "Learning Java", Instant.parse("2024-09-30T11:00:00Z")));
        tweets.add(new Tweet(3, "user1", "Java is great", Instant.parse("2024-09-30T12:00:00Z")));

        List<Tweet> result = Filter.writtenBy(tweets, "user1");

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId()); // First tweet should be by user1
        assertEquals(3, result.get(1).getId()); // Second tweet should be by user1
    }

    @Test
    public void testWrittenByNoMatch() {
     

        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet(1, "user1", "Hello World", Instant.parse("2024-09-30T10:00:00Z")));
        tweets.add(new Tweet(2, "user2", "Learning Java", Instant.parse("2024-09-30T11:00:00Z")));

        List<Tweet> result = Filter.writtenBy(tweets, "user3");

        assertEquals(0, result.size()); // No tweets should match
    }

    @Test
    public void testWrittenByEmptyList() {
       
        List<Tweet> tweets = new ArrayList<>();
        List<Tweet> result = Filter.writtenBy(tweets, "user1");

        assertEquals(0, result.size()); // No tweets should match since the list is empty
    }

    @Test
    public void testWrittenByCaseSensitivity() {
       
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet(1, "User1", "Hello World", Instant.parse("2024-09-30T10:00:00Z")));
        tweets.add(new Tweet(2, "user2", "Learning Java", Instant.parse("2024-09-30T11:00:00Z")));
        tweets.add(new Tweet(3, "USER1", "Java is great", Instant.parse("2024-09-30T12:00:00Z")));

        List<Tweet> result = Filter.writtenBy(tweets, "user1");

        assertEquals(0, result.size()); // No tweets should match since it's case-sensitive
    }
    
    @Test
    public void testInTimespan() {

        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet(1, "user1", "Hello World", Instant.parse("2024-09-30T10:00:00Z")));
        tweets.add(new Tweet(2, "user2", "Learning Java", Instant.parse("2024-09-30T11:00:00Z")));
        tweets.add(new Tweet(3, "user1", "Java is great", Instant.parse("2024-09-30T12:00:00Z")));
        tweets.add(new Tweet(4, "user2", "Outside the timespan", Instant.parse("2024-09-30T09:00:00Z")));

        Timespan timespan = new Timespan(Instant.parse("2024-09-30T10:00:00Z"), Instant.parse("2024-09-30T12:00:00Z"));
        List<Tweet> result = Filter.inTimespan(tweets, timespan);

        assertEquals(3, result.size()); // Should return 3 tweets within the timespan
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
        assertEquals(3, result.get(2).getId());
    }

    @Test
    public void testInTimespanNoMatch() {

        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet(1, "user1", "Hello World", Instant.parse("2024-09-30T09:00:00Z")));
        tweets.add(new Tweet(2, "user2", "Learning Java", Instant.parse("2024-09-30T09:30:00Z")));

        Timespan timespan = new Timespan(Instant.parse("2024-09-30T10:00:00Z"), Instant.parse("2024-09-30T12:00:00Z"));
        List<Tweet> result = Filter.inTimespan(tweets, timespan);

        assertEquals(0, result.size()); // No tweets should match
    }

    @Test
    public void testInTimespanBoundary() {

        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet(1, "user1", "Start of timespan", Instant.parse("2024-09-30T10:00:00Z")));
        tweets.add(new Tweet(2, "user2", "Inside timespan", Instant.parse("2024-09-30T11:00:00Z")));
        tweets.add(new Tweet(3, "user1", "End of timespan", Instant.parse("2024-09-30T12:00:00Z")));
        tweets.add(new Tweet(4, "user2", "Outside the timespan", Instant.parse("2024-09-30T12:01:00Z")));

        Timespan timespan = new Timespan(Instant.parse("2024-09-30T10:00:00Z"), Instant.parse("2024-09-30T12:00:00Z"));
        List<Tweet> result = Filter.inTimespan(tweets, timespan);

        assertEquals(3, result.size()); // Should return 3 tweets within the timespan
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
        assertEquals(3, result.get(2).getId());
    }

    @Test
    public void testInTimespanEmptyList() {

        List<Tweet> tweets = new ArrayList<>();
        Timespan timespan = new Timespan(Instant.parse("2024-09-30T10:00:00Z"), Instant.parse("2024-09-30T12:00:00Z"));
        List<Tweet> result = Filter.inTimespan(tweets, timespan);

        assertEquals(0, result.size()); // No tweets should match since the list is empty
    }
    

    @Test
    public void testContainingNoMatch() {
       
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet(1, "user1", "Hello World", Instant.parse("2024-09-30T10:00:00Z")));
        tweets.add(new Tweet(2, "user2", "Learning C++", Instant.parse("2024-09-30T11:00:00Z")));

        List<String> words = Arrays.asList("java", "python");
        List<Tweet> result = Filter.containing(tweets, words);

        assertEquals(0, result.size()); // No tweets should match
    }

    @Test
    public void testContainingCaseInsensitive() {
        
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet(1, "user1", "Hello World", Instant.parse("2024-09-30T10:00:00Z")));
        tweets.add(new Tweet(2, "user2", "Learning Java", Instant.parse("2024-09-30T11:00:00Z")));
        tweets.add(new Tweet(3, "user1", "I love programming", Instant.parse("2024-09-30T12:00:00Z")));
        tweets.add(new Tweet(4, "user2", "Just another day", Instant.parse("2024-09-30T09:00:00Z")));

        List<String> words = Arrays.asList("HELLO", "java");
        List<Tweet> result = Filter.containing(tweets, words);

        assertEquals(2, result.size()); // Should return 2 tweets
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
    }

    @Test
    public void testContainingMultipleMatches() {
      
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet(1, "user1", "Hello World", Instant.parse("2024-09-30T10:00:00Z")));
        tweets.add(new Tweet(2, "user2", "Learning Python", Instant.parse("2024-09-30T11:00:00Z")));
        tweets.add(new Tweet(3, "user1", "Java is great", Instant.parse("2024-09-30T12:00:00Z")));
        tweets.add(new Tweet(4, "user2", "Just another tweet", Instant.parse("2024-09-30T09:00:00Z")));

        List<String> words = Arrays.asList("Hello", "great");
        List<Tweet> result = Filter.containing(tweets, words);

        assertEquals(2, result.size()); // Should return 2 tweets
        assertEquals(1, result.get(0).getId());
        assertEquals(3, result.get(1).getId());
    }

    @Test
    public void testContainingEmptyList() {
      
        List<Tweet> tweets = new ArrayList<>();
        List<String> words = Arrays.asList("hello", "java");
        List<Tweet> result = Filter.containing(tweets, words);

        assertEquals(0, result.size()); // No tweets should match since the list is empty
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

}

/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */
    
    @Test
    public void testGuessFollowsGraphEmptyTweets() {
        List<Tweet> tweets = new ArrayList<>();
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("Expected an empty graph for empty tweet list", graph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphNoMentions() {
        List<Tweet> tweets = List.of(new Tweet(1, "alice", "hello world", Instant.now()));
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue( "Expected an empty graph when no mentions are present",graph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphSingleMention() {
        List<Tweet> tweets = List.of(new Tweet(1, "alice", "hello @bob", Instant.now()));
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(graph.containsKey("alice"));
        assertTrue("Expected alice to follow bob",graph.get("alice").contains("bob"));
    }
    
    @Test
    public void testGuessFollowsGraphMultipleMentions() {
        List<Tweet> tweets = List.of(new Tweet(1, "alice", "hi @bob @charlie", Instant.now()));
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(graph.containsKey("alice"));
        assertTrue(graph.get("alice").contains("bob"));
        assertTrue(graph.get("alice").contains("charlie"));
    }

    @Test
    public void testGuessFollowsGraphMultipleTweetsSameAuthor() {
        List<Tweet> tweets = List.of(
            new Tweet(1, "alice", "hi @bob", Instant.now()),
            new Tweet(2, "alice", "hello @charlie", Instant.now())
        );
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(graph.get("alice").containsAll(Set.of("bob", "charlie")));
    }


    @Test
    public void testInfluencersEmptyFollowsGraph() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue( "Expected no influencers in an empty graph",influencers.isEmpty());
    }
    
    @Test
    public void testInfluencersSingleUserNoFollowers() {
        Map<String, Set<String>> followsGraph = Map.of("alice", Set.of());
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("Expected no influencers when user has no followers",influencers.isEmpty());
    }

    @Test
    public void testInfluencersSingleInfluencer() {
        Map<String, Set<String>> followsGraph = Map.of(
            "alice", Set.of("bob"),
            "charlie", Set.of("bob")
        );
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("Expected bob to be the top influencer",List.of("bob"), influencers);
    }
    
    @Test
    public void testInfluencersMultipleInfluencers() {
        Map<String, Set<String>> followsGraph = Map.of(
            "alice", Set.of("bob", "charlie"),
            "david", Set.of("charlie"),
            "edward", Set.of("bob")
        );
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals(List.of("bob", "charlie"), influencers.subList(0, 2));
    }
    
    @Test
    public void testInfluencersEqualFollowers() {
        Map<String, Set<String>> followsGraph = Map.of(
            "alice", Set.of("bob", "charlie"),
            "david", Set.of("bob", "charlie")
        );
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.containsAll(List.of("bob", "charlie")));
    }

}

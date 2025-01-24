/**
 * Stack Overflow
 * Users-> admin, member, Guests
 * Guests can see content, search but register to add content 
 * Member > add comment, remove comment, edit comment, add question upvotes, downvotes, flag questions
 * bounty, badges, tags
 * Moderator -> close question,open question, see flaged questions
 * Notification -> notify users when edited, commented, voted, constant news updates
 * Search by category, question
 * System - Most freq used tags 
 * 
 */

class abstract Users{
    private String name;
    private String emailID;
    private String password;

    public List<Content> getSearchContentByTags(Tags tag){
        for()
    }
    public List<Content> getFrequentlyAccessedContent();

    // getter setter methods
}

class Member extends Users{

}
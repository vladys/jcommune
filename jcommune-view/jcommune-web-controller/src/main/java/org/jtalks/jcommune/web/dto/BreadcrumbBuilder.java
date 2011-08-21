package org.jtalks.jcommune.web.dto;

import org.jtalks.jcommune.model.entity.*;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO builder for {@link org.jtalks.jcommune.web.dto.Breadcrumb} objects.
 * Used for preparing breadcrumbs for the different JSP views.
 *
 * @author Alexandre Teterin
 */

public class BreadcrumbBuilder {

    //represent the location breadcrumb list
    private List<Breadcrumb> breadcrumbList;

    /**
     * Returns the Forum breadcrumbs.
     *
     * @return the breadcrumb list for the Forum location. Contains one (root) breadcrumb.
     */
    public List<Breadcrumb> getForumBreadcrumb() {
        breadcrumbList = new ArrayList<Breadcrumb>();
        breadcrumbList.add(prepareForumBreadcrumb());

        return breadcrumbList;
    }

    /**
     * Returns the section breadcrumbs.
     *
     * @param section {@link org.jtalks.jcommune.model.entity.Section} the breadcrumbed section.
     * @return the breadcrumb list for the current <code>Section</code> location.
     */
    public List<Breadcrumb> getSectionBreadcrumb(Section section) {
        breadcrumbList = getForumBreadcrumb();
        breadcrumbList.add(prepareSectionBreadcrumb(section));

        return breadcrumbList;
    }

    /**
     * Returns the branch breadcrumbs.
     *
     * @param branch {@link org.jtalks.jcommune.model.entity.Branch} the breadcrumbed branch.
     * @return the breadcrumb list for the current <code>Branch</code> location.
     */
    public List<Breadcrumb> getBranchBreadcrumb(Branch branch) {
        breadcrumbList = getSectionBreadcrumb(branch.getSection());
        breadcrumbList.add(prepareBranchBreadcrumb(branch));

        return breadcrumbList;
    }

    /**
     *Fill the forum breadcrumb.
     *
     * @return {@link Breadcrumb} the filled breadcrumb fot the Forum location.
     */
    private Breadcrumb prepareForumBreadcrumb() {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setId(Breadcrumb.STUB_BREADCRUMB_ID);
        breadcrumb.setBreadcrumbLocation(Breadcrumb.BreadcrumbLocation.FORUM);
        breadcrumb.setBreadcrumbLocationValue(Breadcrumb.ROOT_BREADCRUMB_LOCATION_VALUE);

        return breadcrumb;
    }


    /**
     *Fill the section breadcrumb.
     *
     * @param section {@link org.jtalks.jcommune.model.entity.Section} the breadcrumbed section.
     * @return {@link Breadcrumb} the filled breadcrumb fot the Forum location.
     */
    private Breadcrumb prepareSectionBreadcrumb(Section section) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setId(section.getId());
        breadcrumb.setBreadcrumbLocation(Breadcrumb.BreadcrumbLocation.SECTION);
        breadcrumb.setBreadcrumbLocationValue(section.getName());

        return breadcrumb;
    }

    /**
     *Fill the branch breadcrumb.
     *
     * @param branch {@link org.jtalks.jcommune.model.entity.Branch} the breadcrumbed branch.
     * @return {@link Breadcrumb} the filled breadcrumb fot the Section location.
     */
    private Breadcrumb prepareBranchBreadcrumb(Branch branch) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setId(branch.getId());
        breadcrumb.setBreadcrumbLocation(Breadcrumb.BreadcrumbLocation.BRANCH);
        breadcrumb.setBreadcrumbLocationValue(branch.getName());

        return breadcrumb;
    }

    /**
     *Fill the branch breadcrumb.
     *
     * @param topic {@link org.jtalks.jcommune.model.entity.Topic} the breadcrumbed topic.
     * @return {@link Breadcrumb} the filled breadcrumb fot the Topic location.
     */
    private Breadcrumb prepareTopicBreadcrumb(Topic topic) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setId(topic.getId());
        breadcrumb.setBreadcrumbLocation(Breadcrumb.BreadcrumbLocation.TOPIC);
        breadcrumb.setBreadcrumbLocationValue(topic.getTitle());

        return breadcrumb;
    }

    /**
     * Fill the post breadcrumb.
     * @param post {@link org.jtalks.jcommune.model.entity.Post} the breadcrumbed post.
     * @return {@link Breadcrumb} the filled breadcrumb fot the Post location.
     */
    private Breadcrumb preparePostBreadcrumb(Post post) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setId(post.getId());
        breadcrumb.setBreadcrumbLocation(Breadcrumb.BreadcrumbLocation.POST);
        //TODO Need additional info about display post breadcrumb
        breadcrumb.setBreadcrumbLocationValue("Post ID " + post.getId());

        return breadcrumb;
    }

    /**
     * Fill the profile breadcrumb.
     *
     * @param user {@link org.jtalks.jcommune.model.entity.User} the breadcrumbed user profile.
     * @return {@link Breadcrumb} the filled breadcrumb fot the user profile location.
     */
    private Breadcrumb prepareProfileBreadcrumb(User user) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setId(user.getId());
        breadcrumb.setBreadcrumbLocation(Breadcrumb.BreadcrumbLocation.PROFILE);
        breadcrumb.setBreadcrumbLocationValue(user.getUsername());

        return breadcrumb;
    }

    /**
     * Fill the PM breadcrumb.
     *
     * @return {@link Breadcrumb} the filled breadcrumb fot the PM location.
     */
    private Breadcrumb preparePmBreadcrumb() {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setId(Breadcrumb.STUB_BREADCRUMB_ID);
        breadcrumb.setBreadcrumbLocation(Breadcrumb.BreadcrumbLocation.PRIVATE_MESSAGE);
        breadcrumb.setBreadcrumbLocationValue(Breadcrumb.PM_BREADCRUMB_LOCATION_VALUE);

        return breadcrumb;
    }

    /**
     * Fill the inbox breadcrumb.
     *
     * @return {@link Breadcrumb} the filled breadcrumb fot the inbox location.
     */
    private Breadcrumb prepareInboxBreadcrumb() {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setId(Breadcrumb.STUB_BREADCRUMB_ID);
        breadcrumb.setBreadcrumbLocation(Breadcrumb.BreadcrumbLocation.INBOX);
        breadcrumb.setBreadcrumbLocationValue(Breadcrumb.INBOX_BREADCRUMB_LOCATION_VALUE);

        return breadcrumb;
    }

    /**
     *Fill the outbox breadcrumb.
     *
     * @return {@link Breadcrumb} the filled breadcrumb fot the outbox location.
     */
    private Breadcrumb prepareOutboxBreadcrumb() {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setId(Breadcrumb.STUB_BREADCRUMB_ID);
        breadcrumb.setBreadcrumbLocation(Breadcrumb.BreadcrumbLocation.OUTBOX);
        breadcrumb.setBreadcrumbLocationValue(Breadcrumb.OUTBOX_BREADCRUMB_LOCATION_VALUE);

        return breadcrumb;
    }

    /**
     *Fill the drafts breadcrumb.
     *
     * @return {@link Breadcrumb} the filled breadcrumb fot the drafts location.
     */
    private Breadcrumb prepareDraftsBreadcrumb() {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setId(Breadcrumb.STUB_BREADCRUMB_ID);
        breadcrumb.setBreadcrumbLocation(Breadcrumb.BreadcrumbLocation.DRAFTS);
        breadcrumb.setBreadcrumbLocationValue(Breadcrumb.DRAFTS_BREADCRUMB_LOCATION_VALUE);

        return breadcrumb;
    }

    /**
     * Fill the new PM breadcrumb.
     *
     * @return {@link Breadcrumb} the filled breadcrumb fot the new PM location.
     */
    private Breadcrumb prepareNewPMBreadcrumb() {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setId(Breadcrumb.STUB_BREADCRUMB_ID);
        breadcrumb.setBreadcrumbLocation(Breadcrumb.BreadcrumbLocation.NEW_PM);
        breadcrumb.setBreadcrumbLocationValue(Breadcrumb.NEW_PM_BREADCRUMB_LOCATION_VALUE);

        return breadcrumb;
    }

    /**
     * Fill the draft PM breadcrumb.
     *
     * @param pm {@link org.jtalks.jcommune.model.entity.PrivateMessage} the breadcrumbed draft pm.
     * @return {@link Breadcrumb} the filled breadcrumb fot the draft location.
     */
    private Breadcrumb prepareDraftPmBreadcrumb(PrivateMessage pm) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setId(pm.getId());
        //TODO Need to define standard URI for most location - ${Entity type}/${Entity ID}.html
        // TODO Need to refactor current draft URI (/pm/drafts/${id}.html) to standard type (/pm/drafts/draft/${id}.html)
        breadcrumb.setBreadcrumbLocation(Breadcrumb.BreadcrumbLocation.DRAFT_PM);
        //TODO Need additional info regarding to display draft breadcrumb
        breadcrumb.setBreadcrumbLocationValue(Breadcrumb.DRAFT_PM_BREADCRUMB_LOCATION_VALUE + ": " + pm.getTitle());

        return breadcrumb;
    }
}
package com.mysite.core.models;

import com.adobe.cq.wcm.core.components.models.Component;
import com.adobe.cq.wcm.core.components.models.contentfragment.ContentFragment;
import java.util.List;

/**
 * Defines the {@code Agent CF Model} Sling Model for the {@code /apps/mysite/components/linkedcontentfragment} component.
 */
public interface LinkedContentFragment extends Component {
    /**
     * Returns the Agents List
     *
     * @return the Content Fragment
     */
    default List<ContentFragment> getAgentsList() {
        throw new UnsupportedOperationException();
    }
}

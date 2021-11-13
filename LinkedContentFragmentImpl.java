package com.mysite.core.models.impl;

import com.adobe.cq.wcm.core.components.models.contentfragment.ContentFragment;
import com.adobe.cq.wcm.core.components.models.contentfragment.DAMContentFragment.DAMContentElement;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.mysite.core.models.LinkedContentFragment;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.factory.ModelFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {LinkedContentFragment.class}, resourceType = LinkedContentFragmentImpl.RESOURCE_TYPE)
public class LinkedContentFragmentImpl extends AbstractComponentImpl implements LinkedContentFragment {

    public static final String RESOURCE_TYPE = "mysite/components/linkedcontentfragment";
    private static final String CF_DISPLAY_MODE = "displayMode";
    private static final String CF_ELEMENTS = "elementNames";
    private static final String CF_FRAGMENT_PATH = "fragmentPath";

    @RequestAttribute(injectionStrategy = InjectionStrategy.OPTIONAL)
    private DAMContentElement elementValue;

    @RequestAttribute(injectionStrategy = InjectionStrategy.OPTIONAL)
    private String elements;

    @SlingObject
    private ResourceResolver resourceResolver;

    @OSGiService
    private ModelFactory modelFactory;

    @Override
    public List<ContentFragment> getAgentsList() {
        String[] elementList = elements.split(",");
        String[] agentPaths = elementValue.isMultiValue() ? (String[]) elementValue.getValue() : new String[] { (String) elementValue.getValue() };
        List<ContentFragment> elementsFragmentList = new ArrayList<>();
        Arrays.stream(agentPaths).forEach(agentPath -> {
            elementsFragmentList.add(modelFactory.getModelFromWrappedRequest(request, createLinkedSyntheticResource(agentPath, elementList), ContentFragment.class));
        });
        return elementsFragmentList;
    }

    private ValueMapResource createLinkedSyntheticResource(String path, String ... elementList) {
        ValueMap properties = new ValueMapDecorator(new HashMap<>());
        properties.put(CF_ELEMENTS, elementList);
        properties.put(CF_DISPLAY_MODE, "multi");
        properties.put(CF_FRAGMENT_PATH, path);
        return new ValueMapResource(resourceResolver, resource.getPath(), RESOURCE_TYPE, properties);
    }
}
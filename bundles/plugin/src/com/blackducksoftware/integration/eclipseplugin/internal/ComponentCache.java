package com.blackducksoftware.integration.eclipseplugin.internal;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.blackducksoftware.integration.build.GavWithType;
import com.blackducksoftware.integration.eclipseplugin.hub.ComponentVulnerabilityLookup;
import com.blackducksoftware.integration.eclipseplugin.internal.exception.ComponentLookupNotFoundException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class ComponentCache {

    private LoadingCache<GavWithType, List<Vulnerability>> cache;

    private int cacheCapacity;

    public ComponentCache(final ComponentVulnerabilityLookup vulnLookup, final int cacheCapacity) {
        this.cacheCapacity = cacheCapacity;
        cache = CacheBuilder.newBuilder().maximumSize(cacheCapacity).expireAfterWrite(1, TimeUnit.HOURS)
                .build(new CacheLoader<GavWithType, List<Vulnerability>>() {
                    @Override
                    public List<Vulnerability> load(final GavWithType gav) throws ComponentLookupNotFoundException {
                        if (vulnLookup != null) {
                            return vulnLookup.getVulnsFromComponent(gav.getType().toString(), gav.getGav());
                        } else {
                            throw new ComponentLookupNotFoundException("Unable to look up component in Hub");
                        }
                    }
                });
    }

    public LoadingCache<GavWithType, List<Vulnerability>> getCache() {
        return cache;
    }

    public void setVulnLookup(ComponentVulnerabilityLookup vulnLookup) {
        cache = CacheBuilder.newBuilder().maximumSize(cacheCapacity).expireAfterWrite(1, TimeUnit.HOURS)
                .build(new CacheLoader<GavWithType, List<Vulnerability>>() {
                    @Override
                    public List<Vulnerability> load(final GavWithType gav) throws ComponentLookupNotFoundException {
                        if (vulnLookup != null) {
                            return vulnLookup.getVulnsFromComponent(gav.getType().toString(), gav.getGav());
                        } else {
                            throw new ComponentLookupNotFoundException("Unable to look up component in Hub");
                        }
                    }
                });
    }

}

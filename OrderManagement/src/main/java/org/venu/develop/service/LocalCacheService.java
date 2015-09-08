package org.venu.develop.service;
	 
	import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.LRUMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

	@Repository("lruCache")
public class LocalCacheService<K, T> {
	 
		@Value("${cache.live.time}")
		private String $cache_livetime;
		

		
	    private long timeToLive;
	    private LRUMap cacheMap;
	 
	    protected class CacheObject {
	        public long lastAccessed = System.currentTimeMillis();
	        public T value;
	 
	        protected CacheObject(T value) {
	            this.value = value;
	        }
	    }
	 
	  //200, 500, 6
	    public LocalCacheService() {
	    	this(2000, 5000, 10);
	    	System.out.println("$cache_livetime=???????????????????????????????"+ $cache_livetime);
	    }
	    
	    
	    public LocalCacheService(long timeToLive, final long timerInterval, int maxItems) {
	    	System.out.println("$cache_livetime=???????????????????????????????"+ $cache_livetime);

	        this.timeToLive = timeToLive * 1000;
	 
	        cacheMap = new LRUMap(maxItems);
	 
	        if (timeToLive > 0 && timerInterval > 0) {
	 
	            Thread t = new Thread(new Runnable() {
	                public void run() {
	                    while (true) { //Always run. as this is a daemon thread
	                        try {
	                            Thread.sleep(timerInterval * 1000);
	                        } catch (InterruptedException ex) {
	                        }
	                        cleanup();
	                    }
	                }
	            });
	 
	            t.setDaemon(true);
	            t.start();
	        }
	    }
	 
	    public void put(K key, T value) {
	        synchronized (cacheMap) {
	            cacheMap.put(key, new CacheObject(value));
	        }
	    }
	 
	    @SuppressWarnings("unchecked")
	    public T get(K key) {
	        synchronized (cacheMap) {
	            CacheObject c = (CacheObject) cacheMap.get(key);
	 
	            if (c == null)
	                return null;
	            else {
	                c.lastAccessed = System.currentTimeMillis();
	                return c.value;
	            }
	        }
	    }
	 
	    public void remove(K key) {
	        synchronized (cacheMap) {
	            cacheMap.remove(key);
	        }
	    }
	 
	    public int size() {
	        synchronized (cacheMap) {
	            return cacheMap.size();
	        }
	    }
	 
	    @SuppressWarnings("unchecked")
	    public void cleanup() {
	 
	        long now = System.currentTimeMillis();
	        ArrayList<K> deleteKey = null;
	 
	        synchronized (cacheMap) {
	            MapIterator itr = cacheMap.mapIterator();
	 
	            deleteKey = new ArrayList<K>((cacheMap.size() / 2) + 1);
	            K key = null;
	            CacheObject c = null;
	 
	            while (itr.hasNext()) {
	                key = (K) itr.next();
	                c = (CacheObject) itr.getValue();
	 
	                if (c != null && (now > (timeToLive + c.lastAccessed))) {
	                    deleteKey.add(key);
	                }
	            }
	        }
	 
	        for (K key : deleteKey) {
	            synchronized (cacheMap) {
	                cacheMap.remove(key);
	            }
	 
	            Thread.yield();
	        }
	    }
	    
	    public void printItems(){

	    	if (cacheMap.size()==0) System.out.println(" No elements to print....");
	    	else {
	    		System.out.println("printing ....Now size=" + cacheMap.size());
	    	
	    	Iterator entries = cacheMap.entrySet().iterator();
	    	while (entries.hasNext()) {
	    	  Entry thisEntry = (Entry) entries.next();
	    	  Object key = thisEntry.getKey();
	    	  Object value = thisEntry.getValue();
	    	  //System.out.println("key=" + key.toString() + "; value=" + value.toString());
	    	  System.out.print("  order id=" + key.toString());
	    	}
	    	System.out.println();
	    	}
	    }
}

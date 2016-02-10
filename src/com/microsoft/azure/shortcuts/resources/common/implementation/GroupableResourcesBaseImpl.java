/**
* Copyright (c) Microsoft Corporation
* 
* All rights reserved. 
* 
* MIT License
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files 
* (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, 
* publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
* subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR 
* ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH 
* THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package com.microsoft.azure.shortcuts.resources.common.implementation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.microsoft.azure.shortcuts.common.implementation.EntitiesImpl;
import com.microsoft.azure.shortcuts.resources.implementation.ResourcesImpl;
import com.microsoft.windowsazure.core.ResourceBaseExtended;

public abstract class GroupableResourcesBaseImpl<AZURE, WRAPPER, 
		INNER extends ResourceBaseExtended,
		WRAPPERIMPL extends WRAPPER> extends EntitiesImpl<AZURE> {
	protected GroupableResourcesBaseImpl(AZURE azure) {
		super(azure);
	}
	
	protected abstract List<INNER> getNativeEntities(String group) throws Exception;
	protected abstract INNER getNativeEntity(String group, String name) throws Exception;
	protected abstract WRAPPERIMPL wrap(INNER nativeItem);
	
	public abstract void delete(String groupName, String name) throws Exception;
	
	public final Map<String, WRAPPER> list(String groupName) throws Exception {
		HashMap<String, WRAPPER> wrappers = new HashMap<>();
		for(INNER nativeItem : getNativeEntities(groupName)) {
			wrappers.put(nativeItem.getId(), wrap(nativeItem));
		}
		return Collections.unmodifiableMap(wrappers);
	}
	
	public final Map<String, WRAPPER> list() throws Exception {
		return list(null);
	}
	
	public final WRAPPER get(String groupName, String name) throws Exception {
		return wrap(getNativeEntity(groupName, name));
	}
	
	public final WRAPPER get(String id) throws Exception {
		return get(
			ResourcesImpl.groupFromResourceId(id), 
			ResourcesImpl.nameFromResourceId(id));
	}
	
	public final void delete(String id) throws Exception {
		this.delete(
			ResourcesImpl.groupFromResourceId(id), 
			ResourcesImpl.nameFromResourceId(id));
	}
}

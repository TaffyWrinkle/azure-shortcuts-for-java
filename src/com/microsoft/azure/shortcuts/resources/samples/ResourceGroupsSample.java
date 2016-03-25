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

package com.microsoft.azure.shortcuts.resources.samples;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.microsoft.azure.shortcuts.resources.ResourceGroup;
import com.microsoft.azure.shortcuts.resources.Region;
import com.microsoft.azure.shortcuts.resources.implementation.Azure;

// Tests resources
public class ResourceGroupsSample {
    public static void main(String[] args) {
        try {
            Azure azure = Azure.authenticate("my.azureauth", null);
            test(azure);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    
    public static void test(Azure azure) throws Exception {
		// List resource groups
    	Set<String> groupNames = azure.resourceGroups().list().keySet();
    	System.out.println("Group names: \n\t" + StringUtils.join(groupNames, ",\n\t"));
    	
    	Map<String, ResourceGroup> groups = azure.resourceGroups().list();
    	for(ResourceGroup group : groups.values()) {
    		printGroup(group);
    	}
    	
    	// Create a resource group
    	String groupName = "group" + String.valueOf(System.currentTimeMillis());
    	System.out.println("Creating group " + groupName);
    	azure.resourceGroups().define(groupName)
    		.withRegion(Region.US_WEST)
    		.withTag("hello", "world")
    		.provision();
    	    	
    	// Read a specific resource group
		ResourceGroup resourceGroup = azure.resourceGroups(groupName);
		printGroup(resourceGroup);
		
		// Update a resource group
		azure.resourceGroups().update(groupName)
			.withTag("foo", "bar")
			.withoutTag("hello")
			.apply();
		
		// Delete a specific resource group
		System.out.println("Deleting group " + groupName);
		azure.resourceGroups(groupName).delete();
    }
    

    private static void printGroup(ResourceGroup group) throws Exception {
		System.out.println(String.format("Group: %s\n"
			+ "\tRegion: %s\n"
			+ "\tID: %s\n"
			+ "\tTags: %s\n"
			+ "\tProvisioning state: %s\n",
			group.id(),
			group.region(),
			group.id(),
			group.tags().toString(),
			group.provisioningState()));
    }
}
/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ambari.server.api.handlers;

import org.apache.ambari.server.api.query.Query;
import org.apache.ambari.server.api.query.render.DefaultRenderer;
import org.apache.ambari.server.api.query.render.Renderer;
import org.apache.ambari.server.api.resources.ResourceInstance;
import org.apache.ambari.server.api.services.*;
import org.apache.ambari.server.api.services.persistence.PersistenceManager;
import org.apache.ambari.server.api.util.TreeNode;
import org.apache.ambari.server.controller.spi.Predicate;
import org.apache.ambari.server.controller.spi.RequestStatus;
import org.apache.ambari.server.controller.spi.Resource;
import org.junit.Test;

import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Unit tests for UpdateHandler.
 */
public class UpdateHandlerTest {

  @Test
  public void testHandleRequest__Synchronous_NoPropsInBody() throws Exception {
    Request request = createMock(Request.class);
    RequestBody body = createNiceMock(RequestBody.class);
    ResourceInstance resource = createMock(ResourceInstance.class);
    PersistenceManager pm = createStrictMock(PersistenceManager.class);
    RequestStatus status = createMock(RequestStatus.class);
    Resource resource1 = createMock(Resource.class);
    Resource resource2 = createMock(Resource.class);
    Predicate userPredicate = createNiceMock(Predicate.class);
    Query query = createNiceMock(Query.class);
    Renderer renderer = new DefaultRenderer();

    Set<Resource> setResources = new HashSet<Resource>();
    setResources.add(resource1);
    setResources.add(resource2);

    // expectations
    expect(request.getResource()).andReturn(resource).anyTimes();
    expect(request.getBody()).andReturn(body).anyTimes();
    expect(request.getQueryPredicate()).andReturn(userPredicate).atLeastOnce();
    expect(request.getRenderer()).andReturn(renderer);

    expect(resource.getQuery()).andReturn(query).atLeastOnce();
    query.setRenderer(renderer);
    query.setUserPredicate(userPredicate);

    expect(pm.update(resource, body)).andReturn(status);
    expect(status.getStatus()).andReturn(RequestStatus.Status.Complete);
    expect(status.getAssociatedResources()).andReturn(setResources);
    expect(resource1.getType()).andReturn(Resource.Type.Cluster).anyTimes();
    expect(resource2.getType()).andReturn(Resource.Type.Cluster).anyTimes();

    replay(request, body, resource, pm, status, resource1, resource2, userPredicate, query);

    Result result = new TestUpdateHandler(pm).handleRequest(request);

    assertNotNull(result);
    TreeNode<Resource> tree = result.getResultTree();
    assertEquals(1, tree.getChildren().size());
    TreeNode<Resource> resourcesNode = tree.getChild("resources");
    assertEquals(2, resourcesNode.getChildren().size());
    boolean foundResource1 = false;
    boolean foundResource2 = false;
    for(TreeNode<Resource> child : resourcesNode.getChildren()) {
      Resource r = child.getObject();
      if (r == resource1 && ! foundResource1) {
        foundResource1 = true;
      } else if (r == resource2 && ! foundResource2) {
        foundResource2 = true;
      } else {
        fail();
      }
    }

    assertEquals(ResultStatus.STATUS.OK, result.getStatus().getStatus());
    verify(request, body, resource, pm, status, resource1, resource2, userPredicate, query);
  }

  @Test
  public void testHandleRequest__Synchronous() throws Exception {
    Request request = createMock(Request.class);
    RequestBody body = createNiceMock(RequestBody.class);
    ResourceInstance resource = createMock(ResourceInstance.class);
    PersistenceManager pm = createStrictMock(PersistenceManager.class);
    RequestStatus status = createMock(RequestStatus.class);
    Resource resource1 = createMock(Resource.class);
    Resource resource2 = createMock(Resource.class);
    Predicate userPredicate = createNiceMock(Predicate.class);
    Query query = createNiceMock(Query.class);
    Renderer renderer = new DefaultRenderer();

    Set<Resource> setResources = new HashSet<Resource>();
    setResources.add(resource1);
    setResources.add(resource2);

    // expectations
    expect(request.getResource()).andReturn(resource).anyTimes();
    expect(request.getBody()).andReturn(body).anyTimes();
    expect(request.getQueryPredicate()).andReturn(userPredicate).atLeastOnce();
    expect(request.getRenderer()).andReturn(renderer);

    expect(resource.getQuery()).andReturn(query).atLeastOnce();
    query.setRenderer(renderer);
    query.setUserPredicate(userPredicate);

    expect(pm.update(resource, body)).andReturn(status);
    expect(status.getStatus()).andReturn(RequestStatus.Status.Complete);
    expect(status.getAssociatedResources()).andReturn(setResources);
    expect(resource1.getType()).andReturn(Resource.Type.Cluster).anyTimes();
    expect(resource2.getType()).andReturn(Resource.Type.Cluster).anyTimes();

    replay(request, body, resource, pm, status, resource1, resource2, userPredicate, query);

    Result result = new TestUpdateHandler(pm).handleRequest(request);

    assertNotNull(result);
    TreeNode<Resource> tree = result.getResultTree();
    assertEquals(1, tree.getChildren().size());
    TreeNode<Resource> resourcesNode = tree.getChild("resources");
    assertEquals(2, resourcesNode.getChildren().size());
    boolean foundResource1 = false;
    boolean foundResource2 = false;
    for(TreeNode<Resource> child : resourcesNode.getChildren()) {
      Resource r = child.getObject();
      if (r == resource1 && ! foundResource1) {
        foundResource1 = true;
      } else if (r == resource2 && ! foundResource2) {
        foundResource2 = true;
      } else {
        fail();
      }
    }

    assertEquals(ResultStatus.STATUS.OK, result.getStatus().getStatus());
    verify(request, body, resource, pm, status, resource1, resource2, userPredicate, query);
  }

  @Test
  public void testHandleRequest__Asynchronous() throws Exception {
    Request request = createMock(Request.class);
    RequestBody body = createNiceMock(RequestBody.class);
    ResourceInstance resource = createMock(ResourceInstance.class);
    PersistenceManager pm = createStrictMock(PersistenceManager.class);
    RequestStatus status = createMock(RequestStatus.class);
    Resource resource1 = createMock(Resource.class);
    Resource resource2 = createMock(Resource.class);
    Resource requestResource = createMock(Resource.class);
    Predicate userPredicate = createNiceMock(Predicate.class);
    Query query = createNiceMock(Query.class);
    Renderer renderer = new DefaultRenderer();

    Set<Resource> setResources = new HashSet<Resource>();
    setResources.add(resource1);
    setResources.add(resource2);

    // expectations
    expect(request.getResource()).andReturn(resource).anyTimes();
    expect(request.getBody()).andReturn(body).anyTimes();
    expect(request.getQueryPredicate()).andReturn(userPredicate).atLeastOnce();
    expect(request.getRenderer()).andReturn(renderer);

    expect(resource.getQuery()).andReturn(query).atLeastOnce();
    query.setRenderer(renderer);
    query.setUserPredicate(userPredicate);

    expect(pm.update(resource, body)).andReturn(status);
    expect(status.getStatus()).andReturn(RequestStatus.Status.Accepted);
    expect(status.getAssociatedResources()).andReturn(setResources);
    expect(resource1.getType()).andReturn(Resource.Type.Cluster).anyTimes();
    expect(resource2.getType()).andReturn(Resource.Type.Cluster).anyTimes();
    expect(status.getRequestResource()).andReturn(requestResource).anyTimes();

    replay(request, body, resource, pm, status, resource1, resource2, requestResource, userPredicate, query);

    Result result = new TestUpdateHandler(pm).handleRequest(request);

    assertNotNull(result);
    TreeNode<Resource> tree = result.getResultTree();
    assertEquals(2, tree.getChildren().size());
    TreeNode<Resource> resourcesNode = tree.getChild("resources");
    assertEquals(2, resourcesNode.getChildren().size());
    boolean foundResource1 = false;
    boolean foundResource2 = false;
    for(TreeNode<Resource> child : resourcesNode.getChildren()) {
      Resource r = child.getObject();
      if (r == resource1 && ! foundResource1) {
        foundResource1 = true;
      } else if (r == resource2 && ! foundResource2) {
        foundResource2 = true;
      } else {
        fail();
      }
    }

    TreeNode<Resource> statusNode = tree.getChild("request");
    assertNotNull(statusNode);
    assertEquals(0, statusNode.getChildren().size());
    assertSame(requestResource, statusNode.getObject());
    assertEquals(ResultStatus.STATUS.ACCEPTED, result.getStatus().getStatus());

    verify(request, body, resource, pm, status, resource1, resource2, requestResource, userPredicate, query);
  }

  private class TestUpdateHandler extends UpdateHandler {
    private PersistenceManager m_testPm;

    private TestUpdateHandler(PersistenceManager pm) {
      m_testPm = pm;
    }

    @Override
    protected PersistenceManager getPersistenceManager() {
      return m_testPm;
    }
  }
}
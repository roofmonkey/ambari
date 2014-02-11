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

var App = require('app');

describe('#App.components', function() {

  it('slaves and masters should not intersect', function() {
    var intersected = App.get('components.slaves').filter(function(item){
      return App.get('components.masters').contains(item);
    });
    expect(intersected).to.eql([]);
  });

  it('decommissionAllowed', function() {
    expect(App.get('components.decommissionAllowed')).to.eql(["DATANODE", "TASKTRACKER", "NODEMANAGER", "HBASE_REGIONSERVER"]);
  });

  it('addableToHost', function() {
    expect(App.get('components.addableToHost')).to.eql(["DATANODE", "TASKTRACKER", "NODEMANAGER", "HBASE_REGIONSERVER", "HBASE_MASTER", "ZOOKEEPER_SERVER", "SUPERVISOR"]);
  });

});
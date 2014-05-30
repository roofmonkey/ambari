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

App.SliderAppSummaryController = Ember.Controller.extend({
  appType: function () {
    return this.get('model.appType.displayName');
  }.property('model.appType'),

  sumComponents: function () {
    var componentsResult = [],
    appStatus = this.get('model.status'),
    components = this.get('model.components.content');
    if(!components){
      return [];
    }
    componentsNames = components.mapBy('componentName').uniq();
    componentsNames.splice(componentsNames.indexOf("slider-appmaster"), 1);
    componentsNames.forEach(function (componentName) {
      var live = components.filterBy('componentName', componentName).filterBy('isRunning', true).length,
      total =  components.filterBy('componentName', componentName).length,
      color = (total - live == 0) ? 'green' : 'red';
      color = (appStatus == 'FROZEN') ? '' : color;

      componentsResult.push({
        name : componentName,
        live : live,
        total: total,
        color: color
      });
    });
    return componentsResult;
  }.property('model.components.@each'),

  componentsSection: function () {
    var components = this.get('model.components.content') || [];
    var result = [];
    components.forEach(function (component){
      if(component.get('componentName') != "slider-appmaster"){
        result.push({
          isRunning: component.get('isRunning'),
          host: component.get('host'),
          componentName: component.get('componentName')
        });
      }
    });
    return result;
  }.property('model.components.@each')
});

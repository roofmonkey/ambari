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
var filters = require('views/common/filter_view');
var sort = require('views/common/sort_view');

App.SliderAppSummaryView = App.TableView.extend({

  classNames: ['app_summary'],

  content: function () {
    return this.get('controller.componentsSection');
  }.property('controller.componentsSection.length'),

  didInsertElement: function () {
    this.set('pagination', false);
    this.set('filteredContent',this.get('content'));
  },

  sortView: sort.wrapperView,
  componentNameSort: sort.fieldView.extend({
    column: 0,
    name:'componentName',
    displayName: "Name"
  }),

  hostSort: sort.fieldView.extend({
    column: 1,
    name:'host',
    displayName: "Host"
  }),

  /**
   * associations between host property and column index
   * @type {Array}
   */
  colPropAssoc: function(){
    var associations = [];
    associations[0] = 'componentName';
    associations[1] = 'host';
    return associations;
  }.property(),


  /**
   * List of graphs shown on page
   * Format:
   * <code>
   *   [
   *      {
   *        id: string,
   *        view: App.AppMetricView
   *      },
   *      {
   *        id: string,
   *        view: App.AppMetricView
   *      },
   *      ....
   *   ]
   * </code>
   * @type {{object}[][]}
   */
  graphs: [],

  /**
   * Update <code>graphs</code>-list when <code>model</code> is updated
   * New metrics are pushed to <code>graphs</code> (not set new list to <code>graphs</code>!) to prevent page flickering
   * @method updateGraphs
   */
  updateGraphs: function() {
    var model = this.get('controller.model'),
      existingGraphs = this.get('graphs');
    if (model) {
      var currentGraphIds = [];
      var supportedMetrics = model.get('supportedMetricNames');
      if (supportedMetrics && supportedMetrics.length > 0) {
        var appId = model.get('id');
        supportedMetrics.split(',').forEach(function(metricName) {
          var graphId = metricName + '_' + appId;
          currentGraphIds.push(graphId);
          if (!existingGraphs.isAny('id', graphId)) {
            var view = App.AppMetricView.extend({
              app: model,
              metricName: metricName
            });
            existingGraphs.push({
              id : graphId,
              view : view
            });
          }
        });
      }
      // Delete not existed graphs
      var toDeleteGraphs = [];
      existingGraphs.forEach(function(existingGraph) {
       if (currentGraphIds.indexOf(existingGraph.id) == -1) {
          toDeleteGraphs.push(existingGraph);
        }
      });
      if(toDeleteGraphs.length > 0) {
        var newGraphs = existingGraphs;
        toDeleteGraphs.forEach(function(toDeleteGraph) {
          newGraphs = newGraphs.without(toDeleteGraph);
        });
        this.set('graphs', newGraphs);
      }
    }
  }.observes('controller.model.supportedMetricNames')
});

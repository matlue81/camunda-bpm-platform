/* global ngDefine: false */
ngDefine('cockpit.directives', [ 'angular' ], function(module, angular) {
  'use strict';
  var _breadcrumbs = [];

  function DirectiveController($scope, $element, $attrs, $rootScope, ProcessInstanceResource, ProcessDefinitionResource) {

    $rootScope.page = {
      breadcrumbs: ,
      title: $rootScope.page && $rootScope.page.title ? $rootScope.page.title : ''
    };

    $scope.expand = function (breadcrumb) {
      if (breadcrumb.processInstance) {
        $rootScope.page.breadcrumbs.splice($rootScope.page.breadcrumbs.indexOf(breadcrumb), 1);
        fetchAllSuperProcessInstances(breadcrumb.processInstance.id);
      }
    };

    $rootScope.clearBreadcrumbs = function () {
      $rootScope.page.breadcrumbs = [];
    };

    $rootScope.addBreadcrumb = function (breadcrumb) {

      switch (breadcrumb.type) {
        case 'processDefinition':
          var processDefinition = breadcrumb.processDefinition;
          breadcrumb.label = processDefinition.name || processDefinition.key || processDefinition.id;
          breadcrumb.href = '/process-definition/' + processDefinition.id;
          breadcrumb.divider = '/';
          $rootScope.page.breadcrumbs.push(breadcrumb);
          return;
        case 'processInstance':
          var processInstance = breadcrumb.processInstance;
          var processDefinition = breadcrumb.processDefinition;

          breadcrumb.label = processInstance.id;
          breadcrumb.href = '/process-instance/' + processInstance.id;
          breadcrumb.divider = ':';

          ProcessInstanceResource.count({ subProcessInstance: processInstance.id }).$then(function(response) {
            var count = response.data.count;
            if (count === 1) {
              $rootScope.page.breadcrumbs.unshift({ type: 'expand', divider: '/', processInstance: processInstance});
            }
          });

          $rootScope.page.breadcrumbs.push(breadcrumb);
          return;
      }
    };

    function fetchAllSuperProcessInstances (subProcessInstanceId) {
      ProcessInstanceResource.query({'subProcessInstance': subProcessInstanceId}).$then(function (response) {

        if (response.data.length > 0) {
          var superProcessInstance = response.data[0];

          ProcessDefinitionResource.get({'id': superProcessInstance.definitionId}).$then(function (response) {
            var processDefinition = response.data;

            var processDefinitionBreadcrumb = {
              'type': 'processDefinition',
              'processDefinition': processDefinition,
              'label': processDefinition.name || processDefinition.key || processDefinition.id,
              'href': '/process-definition/' + processDefinition.id,
              'divider': '/'
            };

            var processInstanceBreadcrumb = {
              'type': 'processInstance',
              'processDefinition': processDefinition,
              'processInstance': superProcessInstance,
              'label': superProcessInstance.id,
              'divider': ':',
              'href': '/process-instance/' + superProcessInstance.id
            };

            $rootScope.page.breadcrumbs.unshift(processInstanceBreadcrumb);
            $rootScope.page.breadcrumbs.unshift(processDefinitionBreadcrumb);

            return fetchAllSuperProcessInstances(superProcessInstance.id);
          });
        }
      });
    }
  }

  var breadcrumpsTemplate =
    '<ul class="breadcrumb">' +
      '<li>' +
        '<a href="#">Home</a>' +
      '</li>' +
      '<li ng-repeat="breadcrumb in breadcrumbs" ng-class="{ active: $last }" ng-switch="breadcrumb.type">' +
        '<span class="divider">{{ breadcrumb.divider }}</span>' +
        '<span ng-switch-when="processDefinition">' +
         '<a ng-if="!$last" href="#{{ breadcrumb.href }}">{{ breadcrumb.label }}</a>' +
         '<span ng-if="$last">{{breadcrumb.label}}</span>' +
        '</span>' +
        '<span ng-switch-when="processInstance">' +
         '<a ng-if="!$last" href="#{{ breadcrumb.href }}" title="{{ breadcrumb.label }}">{{breadcrumb.label | shorten:8 }}</a>' +
          '<span ng-if="$last" title="{{ breadcrumb.label }}">{{ breadcrumb.label | shorten:8 }}</span>' +
        '</span>' +
        '<span ng-switch-when="expand">' +
          '<a ng-click="expand(breadcrumb)" href title="Expand">...</a>' +
        '</span>' +
      '</li>' +
    '</ul>';

  var Directive = function (ProcessInstanceResource, ProcessDefinitionResource) {
    return {
      restrict: 'EA',
      template: breadcrumpsTemplate,
      controller: DirectiveController
    };
  };

  Directive.$inject = [ 'ProcessInstanceResource', 'ProcessDefinitionResource' ];

  module
    .directive('camBreadcrumbsPanel', Directive);

});

/* global ngDefine: false */
ngDefine('cockpit.directives', [ 'angular' ], function(module, angular) {
  'use strict';
  var _breadcrumbs = [];

  function DirectiveController($rootScope) {
    $rootScope.page = $rootScope.page || {};
  }

  var breadcrumpsTemplate =
    '<ul class="breadcrumb">' +
      '<li>' +
        '<a href="#">Home</a>' +
      '</li>' +

      '<li ng-repeat="breadcrumb in $root.page.breadcrumbs" ng-class="{ active: $last }" ng-switch="breadcrumb.type">' +
        '<span class="divider">{{ divider }}</span>' +
        '<span ng-switch-when="processDefinition">' +
          '<a ng-if="!$last" ng-href="{{ breadcrumb.href }}">{{ breadcrumb.label }}</a>' +
          '<span ng-if="$last">{{ breadcrumb.label }}</span>' +
        '</span>' +
        '<span ng-switch-when="processInstance">' +
          '<a ng-if="!$last" ng-href="{{ breadcrumb.href }}" title="{{ breadcrumb.label }}">{{breadcrumb.label | shorten:8 }}</a>' +
          '<span ng-if="$last" title="{{ breadcrumb.label }}">{{ breadcrumb.label | shorten:8 }}</span>' +
        '</span>' +
      '</li>' +

    '</ul>';

  module.directive('camBreadcrumbsPanel', [
    '$rootScope',
  function ($rootScope) {
    return {
      scope: {
        divider: '@'
      },
      link: function(scope) {
        scope.divider = scope.divider || ' | '; // '»';
      },

      restrict: 'A',
      template: breadcrumpsTemplate,
      controller: DirectiveController,
    };
  }]);
});

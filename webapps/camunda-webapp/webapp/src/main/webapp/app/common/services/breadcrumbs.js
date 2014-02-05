/* global ngDefine: false */

ngDefine('camunda.common.services.breadcrumbs', function(module) {
  'use strict';

  /**
   * A service to manage a page breadcrumbs.
   *
   * @name breadcrumbs
   * @memberof cam.common.services
   * @type angular.factory
   */
  module.factory('breadcrumbs', function() {
    var crumbs = [];
    return {
      add: function(crumb) {
        crumbs.push(crumb);
      },

      get: function() {
        return crumbs;
      },

      clear: function() {
        crumbs = [];
      }
    };
  });
});

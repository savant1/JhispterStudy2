(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('NoterDetailController', NoterDetailController);

    NoterDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Noter', 'Ouvrage', 'Utilisateur'];

    function NoterDetailController($scope, $rootScope, $stateParams, entity, Noter, Ouvrage, Utilisateur) {
        var vm = this;

        vm.noter = entity;

        var unsubscribe = $rootScope.$on('projetApp:noterUpdate', function(event, result) {
            vm.noter = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

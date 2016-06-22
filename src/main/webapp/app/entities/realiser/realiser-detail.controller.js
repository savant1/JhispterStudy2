(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('RealiserDetailController', RealiserDetailController);

    RealiserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Realiser', 'Ouvrage', 'Auteur'];

    function RealiserDetailController($scope, $rootScope, $stateParams, entity, Realiser, Ouvrage, Auteur) {
        var vm = this;

        vm.realiser = entity;

        var unsubscribe = $rootScope.$on('projetApp:realiserUpdate', function(event, result) {
            vm.realiser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

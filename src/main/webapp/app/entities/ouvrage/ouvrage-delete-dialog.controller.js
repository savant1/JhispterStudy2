(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('OuvrageDeleteController',OuvrageDeleteController);

    OuvrageDeleteController.$inject = ['$uibModalInstance', 'entity', 'Ouvrage'];

    function OuvrageDeleteController($uibModalInstance, entity, Ouvrage) {
        var vm = this;

        vm.ouvrage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Ouvrage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

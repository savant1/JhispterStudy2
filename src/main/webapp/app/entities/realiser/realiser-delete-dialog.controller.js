(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('RealiserDeleteController',RealiserDeleteController);

    RealiserDeleteController.$inject = ['$uibModalInstance', 'entity', 'Realiser'];

    function RealiserDeleteController($uibModalInstance, entity, Realiser) {
        var vm = this;

        vm.realiser = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Realiser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

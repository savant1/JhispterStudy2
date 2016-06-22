(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('RecommanderDeleteController',RecommanderDeleteController);

    RecommanderDeleteController.$inject = ['$uibModalInstance', 'entity', 'Recommander'];

    function RecommanderDeleteController($uibModalInstance, entity, Recommander) {
        var vm = this;

        vm.recommander = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Recommander.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

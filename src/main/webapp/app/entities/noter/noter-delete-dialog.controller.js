(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('NoterDeleteController',NoterDeleteController);

    NoterDeleteController.$inject = ['$uibModalInstance', 'entity', 'Noter'];

    function NoterDeleteController($uibModalInstance, entity, Noter) {
        var vm = this;

        vm.noter = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Noter.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('NoterDialogController', NoterDialogController);

    NoterDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Noter', 'Ouvrage', 'Utilisateur'];

    function NoterDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Noter, Ouvrage, Utilisateur) {
        var vm = this;

        vm.noter = entity;
        vm.clear = clear;
        vm.save = save;
        vm.ouvrages = Ouvrage.query({filter: 'noter-is-null'});
        $q.all([vm.noter.$promise, vm.ouvrages.$promise]).then(function() {
            if (!vm.noter.ouvrage || !vm.noter.ouvrage.id) {
                return $q.reject();
            }
            return Ouvrage.get({id : vm.noter.ouvrage.id}).$promise;
        }).then(function(ouvrage) {
            vm.ouvrages.push(ouvrage);
        });
        vm.utilisateurs = Utilisateur.query({filter: 'noter-is-null'});
        $q.all([vm.noter.$promise, vm.utilisateurs.$promise]).then(function() {
            if (!vm.noter.utilisateur || !vm.noter.utilisateur.id) {
                return $q.reject();
            }
            return Utilisateur.get({id : vm.noter.utilisateur.id}).$promise;
        }).then(function(utilisateur) {
            vm.utilisateurs.push(utilisateur);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.noter.id !== null) {
                Noter.update(vm.noter, onSaveSuccess, onSaveError);
            } else {
                Noter.save(vm.noter, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projetApp:noterUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

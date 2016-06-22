(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('UtilisateurDialogController', UtilisateurDialogController);

    UtilisateurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Utilisateur', 'Emprunter', 'Commenter', 'Noter', 'Recommander'];

    function UtilisateurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Utilisateur, Emprunter, Commenter, Noter, Recommander) {
        var vm = this;

        vm.utilisateur = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.emprunters = Emprunter.query();
        vm.commenters = Commenter.query();
        vm.noters = Noter.query();
        vm.recommanders = Recommander.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.utilisateur.id !== null) {
                Utilisateur.update(vm.utilisateur, onSaveSuccess, onSaveError);
            } else {
                Utilisateur.save(vm.utilisateur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projetApp:utilisateurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setPicture = function ($file, utilisateur) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        utilisateur.picture = base64Data;
                        utilisateur.pictureContentType = $file.type;
                    });
                });
            }
        };

    }
})();

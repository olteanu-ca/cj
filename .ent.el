;;; .ent.el --- local ent config file -*- lexical-binding: t; -*-

;;; Commentary:

;;; Code:

;; project settings
(setq ent-project-home (file-name-directory (if load-file-name load-file-name buffer-file-name)))
(setq ent-project-name "remarkable2")
(setq ent-clean-regexp "~$\\|\\.tex$")
(setq ent-project-orgfile "README.org")

(require 'ent)

(ent-tasks-init)

(task 'format  '() "run format" '(lambda (&optional x) "clojure -M:format"))

(task 'lint  '() "run lint" '(lambda (&optional x) "bin/lint"))

(task 'test  '() "run tests" '(lambda (&optional x) "bin/test"))

(task 'doc  '() "generate docs" '(lambda (&optional x) "clojure -X:doc"))

(task 'libupdate  '() "updates libs" '(lambda (&optional x) "clojure -M:libupdate"))

(task 'jar  '() "generate uberjar" '(lambda (&optional x) "clojure -X:uberjar"))

(provide '.ent)
;;; .ent.el ends here

;; Local Variables:
;; no-byte-compile: t
;; no-update-autoloads: t
;; End:

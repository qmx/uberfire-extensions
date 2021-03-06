/**
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.uberfire.ext.editor.commons.backend.validation;

import javax.enterprise.context.ApplicationScoped;

import org.uberfire.backend.vfs.Path;

/**
 * Default validation of resource file names
 */
@ApplicationScoped
public class DefaultFileNameValidator implements FileNameValidator {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean accept( final String fileName ) {
        return true;
    }

    @Override
    public boolean accept( final Path path ) {
        return true;
    }

    @Override
    public boolean isValid( final String value ) {
        return ValidationUtils.isFileName( value );
    }

}
